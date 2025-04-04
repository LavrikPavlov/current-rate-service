package ru.kazan.currencyrateservice.domain.dto;

import lombok.*;

@Getter
@Setter
@ToString
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class KafkaTestDto {
    private String topic;
    private String type;
    private Object message;
}
