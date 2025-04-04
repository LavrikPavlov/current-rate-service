package ru.kazan.currencyrateservice.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.kazan.api.generated.model.CurrencyResultDto;
import ru.kazan.api.generated.model.StatusEnum;
import ru.kazan.currencyrateservice.client.CbrFeignClient;
import ru.kazan.currencyrateservice.client.domain.CurrencyResponse;
import ru.kazan.currencyrateservice.client.mapper.CurrencyClientMapper;
import ru.kazan.currencyrateservice.domain.CurrencyEntity;
import ru.kazan.currencyrateservice.domain.dto.KafkaMessageDto;
import ru.kazan.currencyrateservice.repository.CurrencyEntityRepository;
import ru.kazan.currencyrateservice.service.KafkaService;
import ru.kazan.currencyrateservice.service.RateService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static ru.kazan.currencyrateservice.config.KafkaConfig.DEFAULT_KAFKA;
import static ru.kazan.currencyrateservice.config.KafkaConfig.KAFKA_TOPIC_NAME_RATE_REQUEST;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class RateServiceImpl implements RateService {

    @Value(value = "${toggle.scheduler.kafka.limit}")
    private Integer limit;

    private final CbrFeignClient cbrFeignClient;
    private final ObjectMapper objectMapper;
    private final CurrencyClientMapper currencyClientMapper;
    private final CurrencyEntityRepository currencyEntityRepository;
    private final KafkaService kafkaService;


    @Override
    public void updateRates() {
        var response = cbrFeignClient.getCurrency();
        var listCurrency = parseValue(response).getValute().values()
                .stream()
                .map(currencyClientMapper::map)
                .toList();

        var result = currencyEntityRepository.saveAll(processSave(listCurrency));
        log.info("Курс успешно обновлен для {} валют", result.size());
    }

    @Override
    public void sendRates() {
        currencyEntityRepository.findAllByStatusLimit(limit, StatusEnum.WAIT_SEND.getValue())
                .parallelStream()
                .peek(entity -> log.info("Отправка курса {}", entity.getCharCode()))
                .forEach(this::sendKafka);
    }

    @Override
    public void processRates(CurrencyResultDto dto) {
        var rate = currencyEntityRepository.findById(dto.getId())
                .orElseThrow(() -> new EntityNotFoundException(
                        String.format("Курс не найден с id %s", dto.getId())
                ));

        if (Objects.equals(rate.getStatus(), StatusEnum.SEND)) {
            rate.setIsProcessed(true);
            rate.setStatus(dto.getStatus());
            currencyEntityRepository.save(rate);
            log.info("Статус валюты {}, успешно обновлен", rate.getCharCode());
        } else
            log.error("Статус валюты находиться в статусе {}", rate.getStatus());
    }

    private void sendKafka(CurrencyEntity currency) {
        var message = KafkaMessageDto.builder()
                .message(getJson(currencyClientMapper.map(currency)))
                .messageKey(currency.getId())
                .sender(DEFAULT_KAFKA)
                .topic(KAFKA_TOPIC_NAME_RATE_REQUEST)
                .build();
        kafkaService.processMessage(message);
        currency.setStatus(StatusEnum.SEND);
        currency.setIsSent(true);
        currencyEntityRepository.save(currency);
    }

    private List<CurrencyEntity> processSave(List<CurrencyEntity> currencies) {
        var map = currencyEntityRepository.findByIdOrCharCodeIn(
                        currencies.stream().map(CurrencyEntity::getId).toList(),
                        currencies.stream().map(CurrencyEntity::getCharCode).toList()
                ).stream()
                .collect(Collectors.toMap(CurrencyEntity::getId, entity -> entity));

        return currencies.stream()
                .map(currency -> {
                            CurrencyEntity existingEntity = map.get(currency.getId());
                            if (existingEntity != null) {
                                existingEntity.setUpdateDate(LocalDateTime.now());
                                return existingEntity;
                            } else {
                                currency.setStatus(StatusEnum.WAIT_SEND);
                                currency.setCreateDate(LocalDateTime.now());
                                currency.setIsSent(false);
                                currency.setIsProcessed(false);
                                return currency;
                            }
                        }
                ).toList();
    }

    private CurrencyResponse parseValue(String json) {
        try {
            return objectMapper.readValue(json, new TypeReference<>() {
            });
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException(
                    String.format("Ошибка в получении данных о валютах %s", e.getMessage())
            );
        }
    }

    private String getJson(Object obj) {
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Ошибка переобразования обьекта в json", e);
        }
    }

}
