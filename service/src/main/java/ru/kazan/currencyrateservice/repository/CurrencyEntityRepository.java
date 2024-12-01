package ru.kazan.currencyrateservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.kazan.currencyrateservice.domain.CurrencyEntity;

import java.util.List;

@Repository
public interface CurrencyEntityRepository extends JpaRepository<CurrencyEntity, String> {

    @Query(value = " select * from {h-schema}currency c where c.id in :id or c.char_code in :charCode", nativeQuery = true)
    List<CurrencyEntity> findByIdOrCharCodeIn(List<String> id, List<String> charCode);
}
