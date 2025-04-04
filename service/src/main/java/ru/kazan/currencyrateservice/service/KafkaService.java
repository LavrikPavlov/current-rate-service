package ru.kazan.currencyrateservice.service;

import ru.kazan.currencyrateservice.domain.dto.KafkaMessageDto;

public interface KafkaService {

    void processMessage(KafkaMessageDto infoMessage);
}
