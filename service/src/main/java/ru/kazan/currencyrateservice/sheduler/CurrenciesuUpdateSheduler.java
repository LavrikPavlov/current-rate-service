package ru.kazan.currencyrateservice.sheduler;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.kazan.currencyrateservice.service.RateService;

@Service
@RequiredArgsConstructor
public class CurrenciesuUpdateSheduler {

    private final RateService rateService;


    @Scheduled(cron = "* * */12 * *")
    private void updateRate(){
        rateService.updateRates();
    }
}
