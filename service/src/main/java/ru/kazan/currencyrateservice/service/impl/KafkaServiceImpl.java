package ru.kazan.currencyrateservice.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.Header;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.springframework.kafka.core.KafkaOperations;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import ru.kazan.currencyrateservice.domain.dto.KafkaMessageDto;
import ru.kazan.currencyrateservice.service.KafkaService;

import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaServiceImpl implements KafkaService {

    private final Map<String, KafkaOperations<String, String>> templates;

    @Override
    public void processMessage(KafkaMessageDto infoMessage) {
        Optional.ofNullable(templates.get(infoMessage.getSender()))
                .stream()
                .peek(template -> sendMessage(infoMessage, template))
                .findFirst()
                .orElseThrow(() ->
                        new NoSuchElementException("Недосточно информации для отправки сообщения"));
    }

    private void sendMessage(KafkaMessageDto message, KafkaOperations<String, String> template) {
        var recordMessage = new ProducerRecord<>(
                message.getTopic(),
                null,
                message.getMessageKey(),
                message.getMessage(),
                getHeader(message.getMessageHeaders())
        );

        if(template.isTransactional())
            template.executeInTransaction(t ->
                    t.send(recordMessage)
                    .whenCompleteAsync(
                            (result, ex) -> handlerMessage(message.getTopic(), result, ex)
                    )
            );
        else
            template.send(recordMessage)
                    .whenCompleteAsync(
                            (result, ex) -> handlerMessage(message.getTopic(), result, ex)
                    );
    }


    private Iterable<Header> getHeader(Map<String, String> headers) {
        return Objects.nonNull(headers) ? headers.entrySet().stream()
                .map(entry -> new RecordHeader(
                        entry.getKey(),
                        entry.getValue().getBytes(StandardCharsets.UTF_8)
                ))
                .collect(Collectors.toList()) : null;
    }

    private void handlerMessage(String topic, SendResult<String, String> result, Throwable ex) {
        if (Objects.isNull(ex))
            log.info("Сообщение в топик {} отправлено успешно - {}", topic, result);
        else
            log.error("Сообщение в топик {} отправлено успешно - {}", topic, result);
    }

}
