package ru.kazan.currencyrateservice.config;

import feign.Retryer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignConfig {

    @Value(value = "${feign.clients.cbr.timeout}")
    private Long timeout;

    @Value(value = "${feign.clients.cbr.retry}")
    private Integer retry;

    @Value(value = "${feign.clients.cbr.period}")
    private Long period;

    @Bean
    public Retryer feignRetryer() {
        return new Retryer.Default(timeout, period, retry);
    }
}
