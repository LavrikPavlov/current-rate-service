package ru.kazan.currencyrateservice.domain.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import ru.kazan.api.generated.model.CurrencyAllInfoResponse;
import ru.kazan.api.generated.model.CurrencyInfo;
import ru.kazan.api.generated.model.CurrencyInfoResponse;
import ru.kazan.currencyrateservice.domain.CurrencyEntity;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Objects;

@Mapper(componentModel = "spring")
public interface CurrentEntityMapper {

    @Mapping(target = "currency", source = "entities", qualifiedByName = "parseInfoCurrency")
    CurrencyAllInfoResponse map(String mainCurrency, List<CurrencyEntity> entities);

    @Mapping(target = "lastUpdate", expression = "java( mapToOffsetDate( entity ) )")
    CurrencyInfo map(CurrencyEntity entity);

    @Mapping(target = "currency", expression = "java( map( entity ) )")
    CurrencyInfoResponse map(String mainCurrency, CurrencyEntity entity);


    @Named("parseInfoCurrency")
    default List<CurrencyInfo> mapToInfo(List<CurrencyEntity> entities) {
        return entities.stream().map(this::map).toList();
    }

    default OffsetDateTime mapToOffsetDate(CurrencyEntity entity) {
        var zone = ZoneOffset.of("+07:00");
        return Objects.isNull(entity.getUpdateDate()) ?
                OffsetDateTime.of(entity.getCreateDate(), zone) : OffsetDateTime.of(entity.getUpdateDate(), zone);
    }

}
