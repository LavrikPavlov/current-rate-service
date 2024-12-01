package ru.kazan.currencyrateservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.kazan.currencyrateservice.domain.ClientEntity;

import java.util.Optional;

public interface ClientEntityRepository extends JpaRepository<ClientEntity, String> {

    Optional<ClientEntity> findByIpAddress(String ipAddress);
}
