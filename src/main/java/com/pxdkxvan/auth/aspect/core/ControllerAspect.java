package com.pxdkxvan.auth.aspect.core;

import com.pxdkxvan.auth.aspect.AspectPriority;
import com.pxdkxvan.auth.logger.aspect.HTTPAspectLogger;
import com.pxdkxvan.auth.logger.LoggerBuilder;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Order(AspectPriority.HIGHEST)
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
final class ControllerAspect extends LoggingAspect {

    private static final LoggerBuilder.Prefix REQUEST_PREFIX = LoggerBuilder.Prefix.REQUEST;
    private static final LoggerBuilder.Prefix RESPONSE_PREFIX = LoggerBuilder.Prefix.RESPONSE;

    private final HTTPAspectLogger httpAspectLogger;

    @Around("@within(org.springframework.stereotype.Controller) || @within(org.springframework.web.bind.annotation.RestController)")
    Object logAroundControllerMethods(ProceedingJoinPoint joinPoint) throws Throwable {
        return around(joinPoint, httpAspectLogger, REQUEST_PREFIX, RESPONSE_PREFIX);
    }

}
