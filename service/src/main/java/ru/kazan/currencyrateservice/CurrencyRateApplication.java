package ru.kazan.currencyrateservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableScheduling;
import ru.kazan.currencyrateservice.config.banner.CustomApplicationBanner;

@EnableScheduling
@EnableFeignClients
@SpringBootApplication
public class CurrencyRateApplication {

    public static void main(String[] args) {
        var app = new SpringApplication(CurrencyRateApplication.class);
        app.setBanner(new CustomApplicationBanner());
        app.run(args);
    }

}
