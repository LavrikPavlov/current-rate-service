package ru.kazan.currencyrateservice.kafka;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import ru.kazan.api.generated.model.UserResultDto;
import ru.kazan.currencyrateservice.service.ClientService;

import static ru.kazan.currencyrateservice.config.KafkaConfig.KAFKA_TOPIC_NAME_USER_RESPONSE;

@Slf4j
@Component
@RequiredArgsConstructor
@ConditionalOnProperty(value = "toggle.scheduler.kafka.enable", havingValue = "true")
public class ClientListener {

    private final ClientService clientService;

    @KafkaListener(containerFactory = "defaultConsumerFactory", topics = KAFKA_TOPIC_NAME_USER_RESPONSE)
    public void listen(@Payload @Valid UserResultDto user){
        log.info("Вычитано успешно: {}", user);
        clientService.processUser(user);
    }

}
