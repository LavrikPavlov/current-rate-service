package ru.kazan.currencyrateservice.client.domain;

import lombok.Data;
import ru.kazan.api.generated.model.ValuteResponse;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

@Data
public class CurrencyResponse {

    @JsonProperty("Valute")
    private Map<String, ValuteResponse> valute;

    @JsonProperty("Date")
    private String date;

    @JsonProperty("PreviousDate")
    private String previousDate;

    @JsonProperty("PreviousURL")
    private String previousUrl;

    @JsonProperty("Timestamp")
    private String timestamp;

}
