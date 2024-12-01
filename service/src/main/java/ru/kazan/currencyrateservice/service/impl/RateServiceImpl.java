package ru.kazan.currencyrateservice.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.kazan.api.generated.model.HelloResponse;
import ru.kazan.currencyrateservice.client.CbrFeignClient;
import ru.kazan.currencyrateservice.client.domain.CurrencyResponse;
import ru.kazan.currencyrateservice.client.mapper.CurrencyMapper;
import ru.kazan.currencyrateservice.domain.CurrencyEntity;
import ru.kazan.currencyrateservice.repository.CurrencyEntityRepository;
import ru.kazan.currencyrateservice.service.RateService;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class RateServiceImpl implements RateService {

    private final CbrFeignClient cbrFeignClient;
    private final ObjectMapper objectMapper;
    private final CurrencyMapper currencyMapper;
    private final CurrencyEntityRepository currencyEntityRepository;

    @Override
    public HelloResponse makeHello() {
        var response = new HelloResponse();
        response.setIp(getLocalAddress());
        response.setMessage("Hello");
        return response;
    }

    @Override
    @Transactional
    public void updateRates() {
        var response = cbrFeignClient.getCurrency();
        var listCurrency = parseValute(response).getValute().values()
                .stream()
                .map(currencyMapper::map)
                .toList();

        var result = currencyEntityRepository.saveAll(processSave(listCurrency));
        log.info("Курс успешно обновлен для {} валют", result.size());
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
                        currency.setCreateDate(LocalDateTime.now());
                        return currency;
                    }
                }
                ).toList();
    }


    private CurrencyResponse parseValute(String json){
        try {
            return objectMapper.readValue(json, new TypeReference<>() {});
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException(
                    String.format("Ошибка в получении данных о валютах %s", e.getMessage())
            );
        }
    }

    private String getLocalAddress(){
        try {
            return InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            log.error("Ошибка в получении IP");
        }
        return "0.0.0.0";
    }


}
