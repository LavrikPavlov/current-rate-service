package ru.kazan.currencyrateservice.sheduler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.kazan.currencyrateservice.service.RateService;

@Slf4j
@Service
@RequiredArgsConstructor
@ConditionalOnProperty(value = "toggle.scheduler.updateCurrency.enable", havingValue = "true")
public class CurrenciesUpdateScheduler {

    private final RateService rateService;

    @Scheduled(cron = "${toggle.scheduler.updateCurrency.cron}")
    private void updateRate(){
        log.info("Обновление курса валют");
        rateService.updateRates();
    }
}
