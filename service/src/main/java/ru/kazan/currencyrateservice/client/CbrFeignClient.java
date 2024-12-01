package ru.kazan.currencyrateservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "cbrClient", url = "${feign.clients.cbr.url}")
public interface CbrFeignClient {

    @GetMapping("/daily_json.js")
    String getCurrency();

}
