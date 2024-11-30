package ru.kazan.currencyrateservice.api;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.kazan.api.generated.MockApi;
import ru.kazan.api.generated.model.HelloResponse;
import ru.kazan.currencyrateservice.service.RateService;

@Slf4j
@Controller
@RequestMapping("/currency-rate-api")
@RequiredArgsConstructor
public class RateController implements MockApi {

    private final RateService rateService;

    @Override
    public ResponseEntity<HelloResponse> currencyRateV1HelloGet() {
        log.info("API /currency-rate/v1/hello вызвано");
        return ResponseEntity.ok(rateService.makeHello());
    }
}
