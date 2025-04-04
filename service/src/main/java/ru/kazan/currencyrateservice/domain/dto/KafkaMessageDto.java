package ru.kazan.currencyrateservice.domain.dto;

import lombok.*;

import java.util.Map;

@Getter
@Setter
@ToString
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class KafkaMessageDto {
    private String sender;
    private String message;
    private String topic;
    private String messageKey;
    private Map<String, String> messageHeaders;
}
