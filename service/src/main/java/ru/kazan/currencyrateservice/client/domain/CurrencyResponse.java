package ru.kazan.currencyrateservice.client.domain;

import lombok.Data;
import ru.kazan.api.generated.model.ValuteResponse;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

@Data
public class CurrencyResponse {

    @JsonProperty("Valute")
    private Map<String, ValuteResponse> valute;

}
