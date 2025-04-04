package ru.kazan.currencyrateservice.client.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.kazan.api.generated.model.CurrencyInfoDto;
import ru.kazan.api.generated.model.StatusEnum;
import ru.kazan.api.generated.model.ValuteResponse;
import ru.kazan.currencyrateservice.domain.CurrencyEntity;

@Mapper(componentModel = "spring", imports = {StatusEnum.class})
public interface CurrencyClientMapper {

    @Mapping(target = "id", source = "ID")
    CurrencyEntity map(ValuteResponse response);

    CurrencyInfoDto map(CurrencyEntity entity);

}
