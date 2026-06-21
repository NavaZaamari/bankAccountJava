package com.example.bank;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {
    @Before("execution(* com.bank.service.*.*(..))")
    public void logBefore(JoinPoint joinPoint) {
        System.out.println("Method Called");
    }
}
