package com.sparta.vicky.aop;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Slf4j
@Aspect
@Component
public class LoggingAspect {

    @Pointcut("within(@org.springframework.web.bind.annotation.RestController *)")
    private void restController() {}

    @Pointcut("within(@org.springframework.stereotype.Controller *)")
    private void controller() {}

    @Before("restController() || controller()")
    public void logRequestBeforeControllerMethods(JoinPoint joinPoint) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes != null) {
            HttpServletRequest request = attributes.getRequest();
            log.info("Request URL: {}, HTTP Method: {}, Controller: {}, Method: {}",
                    request.getRequestURL().toString(),
                    request.getMethod(),
                    joinPoint.getSignature().getDeclaringTypeName(), // 메서드가 선언된 클래스의 이름
                    joinPoint.getSignature().getName()); // 호출된 메서드의 이름
        }
    }

}