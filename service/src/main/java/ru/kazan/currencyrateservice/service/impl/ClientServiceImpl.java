package ru.kazan.currencyrateservice.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import ru.kazan.api.generated.model.StatusEnum;
import ru.kazan.api.generated.model.UserResultDto;
import ru.kazan.currencyrateservice.domain.ClientEntity;
import ru.kazan.currencyrateservice.domain.RequestEntity;
import ru.kazan.currencyrateservice.domain.dto.KafkaMessageDto;
import ru.kazan.currencyrateservice.domain.mapper.ClientEntityMapper;
import ru.kazan.currencyrateservice.repository.ClientEntityRepository;
import ru.kazan.currencyrateservice.service.ClientService;
import ru.kazan.currencyrateservice.service.KafkaService;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static ru.kazan.currencyrateservice.config.KafkaConfig.DEFAULT_KAFKA;
import static ru.kazan.currencyrateservice.config.KafkaConfig.KAFKA_TOPIC_NAME_USER_REQUEST;

@Slf4j
@Service
@RequiredArgsConstructor
public class ClientServiceImpl implements ClientService {

    @Value(value = "${toggle.scheduler.kafka.limit}")
    private Integer limit;

    private final ClientEntityRepository clientEntityRepository;
    private final ClientEntityMapper clientEntityMapper;
    private final ObjectMapper objectMapper;
    private final KafkaService kafkaService;

    private static final List<String> IP_HEADERS = List.of(
            "X-Forwarded-For",
            "Proxy-Client-IP",
            "WL-Proxy-Client-IP",
            "HTTP_X_FORWARDED_FOR",
            "HTTP_X_FORWARDED",
            "HTTP_X_CLUSTER_CLIENT_IP",
            "HTTP_CLIENT_IP",
            "HTTP_FORWARDED_FOR",
            "HTTP_FORWARDED",
            "HTTP_VIA",
            "REMOTE_ADDR"
    );

    @Async
    @Override
    @Transactional(value = Transactional.TxType.REQUIRES_NEW)
    public void saveClient(HttpServletRequest request) {
        if (Objects.isNull(request))
            return;

        var ip = getClientIp(request);
        var client = clientEntityRepository.findByIpAddress(ip);
        var requestId = request.getHeader("request-id");
        var body = getRequestBody(request);

        if (client.isEmpty()) {
            var newClient = createClient(ip);
            newClient.getRequests().add(createRequest(requestId, body, newClient));
            clientEntityRepository.save(newClient);
        } else {
            var existClient = client.get();
            existClient.getRequests().add(createRequest(requestId, body, existClient));
            clientEntityRepository.save(existClient);
        }

    }

    @Override
    public void sendUser() {
        clientEntityRepository.findAllByStatusLimit(limit, StatusEnum.WAIT_SEND.getValue())
                .parallelStream()
                .peek(entity -> log.info("Отправка курса {}", entity.getIpAddress()))
                .forEach(this::sendKafka);
    }

    @Override
    public void processUser(UserResultDto dto) {
        var user = clientEntityRepository.findById(dto.getId())
                .orElseThrow(() -> new EntityNotFoundException(
                        String.format("Пользователь не найден с id %s", dto.getId())
                ));
        if(Objects.equals(user.getStatus(), StatusEnum.SEND)) {
            user.setIsProcessed(true);
            user.setStatus(dto.getStatus());
            clientEntityRepository.save(user);
        }
    }

    private void sendKafka(ClientEntity client) {
        var message = KafkaMessageDto.builder()
                .message(getJson(clientEntityMapper.map(client)))
                .messageKey(client.getId())
                .sender(DEFAULT_KAFKA)
                .topic(KAFKA_TOPIC_NAME_USER_REQUEST)
                .build();
        kafkaService.processMessage(message);
        client.setStatus(StatusEnum.SEND);
        client.setIsSent(true);
        clientEntityRepository.save(client);
    }

    private ClientEntity createClient(String ipAddress) {
        return ClientEntity.builder()
                .id(UUID.randomUUID().toString().replace("-", ""))
                .ipAddress(ipAddress)
                .firstDate(LocalDateTime.now())
                .lastDate(LocalDateTime.now())
                .requests(new ArrayList<>())
                .isProcessed(false)
                .isSent(false)
                .status(StatusEnum.WAIT_SEND)
                .build();
    }

    private RequestEntity createRequest(String requestId, String body, ClientEntity client) {
        return RequestEntity.builder()
                .id(requestId)
                .requestBody(checkBody(body))
                .date(LocalDateTime.now())
                .client(client)
                .build();
    }

    private String getRequestBody(HttpServletRequest request) {
        try (var reader = request.getReader()) {
            return reader.lines()
                    .collect(
                            Collectors.joining(System.lineSeparator())
                    );
        } catch (IOException e) {
            log.error("Ошибка чтения request body {}", request.getRequestId());
            return null;
        }

    }

    private String getClientIp(HttpServletRequest request) {
        var ip = IP_HEADERS.stream()
                .map(request::getHeader)
                .filter(Objects::nonNull)
                .filter(header -> !header.isEmpty())
                .flatMap(value -> Arrays.stream(value.split("\\s*,\\s*")))
                .findFirst()
                .orElse(request.getRemoteAddr());

        if (check(ip))
            return "127.0.0.1";

        return ip;

    }

    private String checkBody(String body) {
        return body.isEmpty() ? null : body;
    }

    private boolean check(String ip) {
        return Objects.nonNull(ip) && ip.contains(":") && ("0:0:0:0:0:0:0:1".equals(ip) || "::1".equals(ip));
    }

    private String getJson(Object obj) {
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Ошибка переобразования обьекта в json", e);
        }
    }

}
