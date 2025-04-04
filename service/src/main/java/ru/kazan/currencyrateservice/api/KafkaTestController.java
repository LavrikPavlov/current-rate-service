package ru.kazan.currencyrateservice.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.kazan.api.generated.model.CurrencyResultDto;
import ru.kazan.currencyrateservice.domain.dto.KafkaMessageDto;
import ru.kazan.currencyrateservice.domain.dto.KafkaTestDto;
import ru.kazan.currencyrateservice.service.KafkaService;

import static ru.kazan.currencyrateservice.config.KafkaConfig.DEFAULT_KAFKA;

@Slf4j
@RestController
@RequiredArgsConstructor
public class KafkaTestController {

    private final KafkaService kafkaService;
    private final ObjectMapper objectMapper;

    @PostMapping(
            value = "/currency-rate/v1/test/kafka",
            produces = { "application/json" },
            consumes = { "application/json" }
    )
    public void testKafka(@RequestBody KafkaTestDto request){
        log.info("API на проверку кафки вызвана успешно");

        kafkaService.processMessage(
                KafkaMessageDto.builder()
                        .topic(request.getTopic())
                        .message(getMessage(request))
                        .sender(DEFAULT_KAFKA)
                        .build()
        );
        log.info("Сообщение успешно отправлено");
    }

    private String getMessage(KafkaTestDto dto){
        return switch (dto.getType()){
            case "rate", "user" ->  getJson(dto.getMessage());
            default -> throw new IllegalArgumentException("Ошибка типа сообщения");
        };
    }

    private <T> String getJson(T obj){
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Ошибка парсинга обьекта");
        }
    }
}
