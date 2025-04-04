package ru.kazan.currencyrateservice.handler;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.listener.CommonErrorHandler;
import org.springframework.kafka.listener.MessageListenerContainer;

@Slf4j
@Configuration
@NoArgsConstructor
public class KafkaHandler implements CommonErrorHandler {

    @Override
    public boolean handleOne(Exception thrownException, ConsumerRecord<?, ?> record, Consumer<?, ?> consumer, MessageListenerContainer container) {
        log.error("Ошибка {} при вычитывании сообщения {} -> {}",
                thrownException.getMessage(), record.value().toString(), thrownException.getCause().toString()
        );
        return CommonErrorHandler.super.handleOne(thrownException, record, consumer, container);
    }
}
