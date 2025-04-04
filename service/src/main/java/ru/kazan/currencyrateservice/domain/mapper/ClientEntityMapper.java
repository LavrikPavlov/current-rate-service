package ru.kazan.currencyrateservice.domain.mapper;

import org.mapstruct.Mapper;
import ru.kazan.api.generated.model.UserResultDto;
import ru.kazan.currencyrateservice.domain.ClientEntity;

@Mapper(componentModel = "spring")
public interface ClientEntityMapper {

    UserResultDto map(ClientEntity client);
}
