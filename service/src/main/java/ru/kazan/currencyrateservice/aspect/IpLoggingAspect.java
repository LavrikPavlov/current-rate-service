package ru.kazan.currencyrateservice.aspect;

import jakarta.servlet.http.HttpServletRequest;
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

import java.util.Map;
import java.util.Objects;

@Slf4j
@Aspect
@Component
public class IpLoggingAspect {

    private final ClientService clientService;

    private static final String UNKNOWN = "unknown";

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
        var request = attributes.getRequest();
        clientService.saveClient(getClientIp(request), request.getHeader("request-id"));
        return joinPoint.proceed();
    }

    private String getClientIp(HttpServletRequest request){
        String ip = request.getHeader("X-Forwarded-For");
        if(check(ip))
            ip = request.getHeader("Proxy-Client-IP");
        if(check(ip))
            ip = request.getHeader("WL-Proxy-Client-IP");
        if(check(ip))
            ip = request.getRemoteAddr();

        if(!Objects.isNull(ip) || ip.contains(":")){
            if(ip.equals("0:0:0:0:0:0:0:1") || ip.equals("::1"))
                return "127.0.0.1";
        }
        return ip;
    }

    private boolean check(String ip){
        return Objects.isNull(ip) || ip.isEmpty() || UNKNOWN.equalsIgnoreCase(ip);
    }
}
