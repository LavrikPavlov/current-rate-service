package ru.kazan.currencyrateservice.api;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import ru.kazan.api.generated.CurrencyTransactionsApi;
import ru.kazan.api.generated.model.CurrencyAllInfoResponse;
import ru.kazan.api.generated.model.CurrencyInfoResponse;
import ru.kazan.currencyrateservice.service.CurrencyInfoService;

@Slf4j
@Controller
@RequiredArgsConstructor
public class RateController implements CurrencyTransactionsApi {

    private final CurrencyInfoService currencyInfoService;

    @Override
    public ResponseEntity<CurrencyAllInfoResponse> currencyRateV1InfoUrrencyAllGet(String requestId, String mainCurrency) {
        log.info("API /currency-rate/v1/info/сurrency/all вызвана успешно {}", requestId);
        if (mainCurrency.equals("RUB")) {
            return ResponseEntity.ok(currencyInfoService.getAllInfoCurrency(mainCurrency));
        }
        throw new IllegalArgumentException("Обработка валюты пока возможна только RUB");
    }

    @Override
    public ResponseEntity<CurrencyInfoResponse> currencyRateV1InfoUrrencyGet(String requestId, String mainCurrency, String currency) {
        log.info("API /currency-rate/v1/info/сurrency вызвана успешно {}", requestId);
        if (mainCurrency.equals("RUB")) {
            return ResponseEntity.ok(currencyInfoService.getInfoCurrency(mainCurrency, currency));
        }
        throw new IllegalArgumentException("Обработка валюты пока возможна только RUB");
    }
}
