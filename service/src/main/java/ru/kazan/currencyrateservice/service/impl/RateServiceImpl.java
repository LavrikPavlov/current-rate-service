package ru.kazan.currencyrateservice.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.kazan.api.generated.model.HelloResponse;
import ru.kazan.currencyrateservice.service.RateService;

import java.net.InetAddress;
import java.net.UnknownHostException;

@Slf4j
@Service
@RequiredArgsConstructor
public class RateServiceImpl implements RateService {

    @Override
    public HelloResponse makeHello() {
        var response = new HelloResponse();
        response.setIp(getLocalAddress());
        response.setMessage("Hello");
        return response;
    }

    private String getLocalAddress(){
        try {
            return InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            log.error("Ошибка в получении IP");
        }
        return "0.0.0.0";
    }


}
