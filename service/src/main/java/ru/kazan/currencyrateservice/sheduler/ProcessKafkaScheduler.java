package ru.kazan.currencyrateservice.sheduler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.kazan.currencyrateservice.service.ClientService;
import ru.kazan.currencyrateservice.service.RateService;

@Slf4j
@Service
@RequiredArgsConstructor
@ConditionalOnProperty(value = "toggle.scheduler.kafka.enable", havingValue = "true")
public class ProcessKafkaScheduler {

    private final RateService rateService;
    private final ClientService clientService;

    @Scheduled(cron = "${toggle.scheduler.kafka.cron}")
    private void sendRate(){
        rateService.sendRates();
    }

    @Scheduled(cron = "${toggle.scheduler.kafka.cron}")
    private void sendUser(){
        clientService.sendUser();
    }

}
