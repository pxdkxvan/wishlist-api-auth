package com.pxkdxvan.auth.shared.aspect.service;

import com.pxkdxvan.auth.shared.aspect.AspectPriority;
import com.pxkdxvan.auth.shared.logger.aspect.DefaultAspectLogger;
import com.pxkdxvan.auth.shared.logger.LoggerArgs;
import com.pxkdxvan.auth.shared.logger.LoggerBuilder;
import com.pxkdxvan.auth.shared.model.Authorization;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Map;

@Aspect
@Component
@Order(AspectPriority.LOW)
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
final class AuthorizationServiceAspect {

    private static final LoggerBuilder.Prefix SERVICE_PREFIX = LoggerBuilder.Prefix.SERVICE;

    private static final String AUTHORIZATION_KEY = LoggerArgs.AUTHORIZATION_KEY;

    private static final String AFTER_AUTH_CREATE_MESSAGE = "Account authorization created";
    private static final String AFTER_AUTH_GET_OR_CREATE_MESSAGE = "Account authorization obtained";

    private final DefaultAspectLogger defaultAspectLogger;

    @AfterReturning(value = "execution(* com.pxkdxvan.auth.shared.service.AuthorizationService.create*(..))", returning = "auth")
    void logAfterReturningAuthServiceCreateMethod(Authorization auth) {
        Map<String, Object> entries = Collections.singletonMap(AUTHORIZATION_KEY, auth);
        defaultAspectLogger.between(SERVICE_PREFIX, AFTER_AUTH_CREATE_MESSAGE, entries);
    }

    @AfterReturning(value = "execution(* com.pxkdxvan.auth.shared.service.AuthorizationService.get*(..))", returning = "auth")
    void logAfterReturningAuthServiceGetOrCreateMethod(Authorization auth) {
        Map<String, Object> entries = Collections.singletonMap(AUTHORIZATION_KEY, auth);
        defaultAspectLogger.between(SERVICE_PREFIX, AFTER_AUTH_GET_OR_CREATE_MESSAGE, entries);
    }

}
