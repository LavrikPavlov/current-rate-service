package ru.kazan.currencyrateservice.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import ru.kazan.currencyrateservice.domain.ClientEntity;
import ru.kazan.currencyrateservice.domain.RequestEntity;
import ru.kazan.currencyrateservice.repository.ClientEntityRepository;
import ru.kazan.currencyrateservice.service.ClientService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ClientServiceImpl implements ClientService {

    private final ClientEntityRepository clientEntityRepository;

    @Async
    @Override
    @Transactional(value = Transactional.TxType.REQUIRES_NEW)
    public void saveClient(String ipAddress, String requestId) {
        var client = clientEntityRepository.findByIpAddress(ipAddress);

        if (client.isEmpty()) {
            var newClient = createClient(ipAddress);
            newClient.getRequests().add(createRequest(requestId, newClient));
            clientEntityRepository.save(newClient);
        } else {
            var existClient = client.get();
            existClient.getRequests().add(createRequest(requestId, existClient));
            clientEntityRepository.save(existClient);
        }

    }

    private ClientEntity createClient(String ipAddress) {
        return ClientEntity.builder()
                .id(UUID.randomUUID().toString().replace("-", ""))
                .ipAddress(ipAddress)
                .firstDate(LocalDateTime.now())
                .lastDate(LocalDateTime.now())
                .requests(new ArrayList<>())
                .build();
    }

    private RequestEntity createRequest(String requestId, ClientEntity client) {
        return RequestEntity.builder()
                .id(requestId)
                .requestBody(null)
                .date(LocalDateTime.now())
                .client(client)
                .build();
    }

}
