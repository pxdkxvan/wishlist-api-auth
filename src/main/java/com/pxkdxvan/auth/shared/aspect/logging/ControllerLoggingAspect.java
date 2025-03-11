package com.pxkdxvan.auth.shared.aspect.logging;

import com.pxkdxvan.auth.shared.aspect.AspectPriority;
import com.pxkdxvan.auth.shared.logger.aspect.HTTPAspectLogger;
import com.pxkdxvan.auth.shared.logger.LoggerBuilder;

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
final class ControllerLoggingAspect extends LoggingAspect {

    private static final LoggerBuilder.Prefix REQUEST_PREFIX = LoggerBuilder.Prefix.REQUEST;
    private static final LoggerBuilder.Prefix RESPONSE_PREFIX = LoggerBuilder.Prefix.RESPONSE;

    private final HTTPAspectLogger httpAspectLogger;

    @Around("@within(org.springframework.stereotype.Controller) || @within(org.springframework.web.bind.annotation.RestController)")
    Object logAroundControllerMethods(ProceedingJoinPoint joinPoint) throws Throwable {
        return around(joinPoint, httpAspectLogger, REQUEST_PREFIX, RESPONSE_PREFIX);
    }

}
