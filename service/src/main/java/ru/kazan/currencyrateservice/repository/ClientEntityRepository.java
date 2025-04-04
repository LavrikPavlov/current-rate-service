package ru.kazan.currencyrateservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.kazan.currencyrateservice.domain.ClientEntity;

import java.util.List;
import java.util.Optional;

public interface ClientEntityRepository extends JpaRepository<ClientEntity, String> {

    Optional<ClientEntity> findByIpAddress(String ipAddress);

    @Query(value = """
            select * from {h-schema}clients c
            where c.status = :status limit :limit for update skip locked
            """, nativeQuery = true)
    List<ClientEntity> findAllByStatusLimit(Integer limit, String status);
}
