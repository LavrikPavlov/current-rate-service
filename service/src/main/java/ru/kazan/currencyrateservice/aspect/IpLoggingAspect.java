package ru.kazan.currencyrateservice.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import ru.kazan.currencyrateservice.service.ClientService;

import java.util.Objects;

@Slf4j
@Aspect
@Component
public class IpLoggingAspect {

    private final ClientService clientService;

    @Autowired
    public IpLoggingAspect(ClientService clientService) {
        this.clientService = clientService;
    }

    @Pointcut("within(@org.springframework.stereotype.Controller *)")
    public void controllerMethods() {
    }

    @Around("controllerMethods()")
    public Object logIpAddress(ProceedingJoinPoint joinPoint) throws Throwable {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (Objects.nonNull(attributes))
            clientService.saveClient(attributes.getRequest());
        return joinPoint.proceed();
    }
}
