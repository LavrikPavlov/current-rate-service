package ru.kazan.currencyrateservice.kafka;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import ru.kazan.api.generated.model.CurrencyResultDto;
import ru.kazan.api.generated.model.UserResultDto;
import ru.kazan.currencyrateservice.service.RateService;

import static ru.kazan.currencyrateservice.config.KafkaConfig.KAFKA_TOPIC_NAME_RATE_RESPONSE;
import static ru.kazan.currencyrateservice.config.KafkaConfig.KAFKA_TOPIC_NAME_USER_RESPONSE;

@Slf4j
@Component
@RequiredArgsConstructor
@ConditionalOnProperty(value = "toggle.scheduler.kafka.enable", havingValue = "true")
public class CurrencyListener {

    private final RateService rateService;

    @KafkaListener(containerFactory = "defaultConsumerFactory", topics = KAFKA_TOPIC_NAME_RATE_RESPONSE)
    public void listen(@Payload @Valid CurrencyResultDto currency){
        log.info("Вычитано успешно: {}", currency);
        rateService.processRates(currency);
    }

}
