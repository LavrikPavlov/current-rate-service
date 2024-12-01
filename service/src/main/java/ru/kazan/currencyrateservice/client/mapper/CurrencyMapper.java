package ru.kazan.currencyrateservice.client.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.kazan.api.generated.model.ValuteResponse;
import ru.kazan.currencyrateservice.domain.CurrencyEntity;

@Mapper(componentModel = "spring")
public interface CurrencyMapper {

    @Mapping(target = "id", source = "ID")
    CurrencyEntity map(ValuteResponse response);

}
