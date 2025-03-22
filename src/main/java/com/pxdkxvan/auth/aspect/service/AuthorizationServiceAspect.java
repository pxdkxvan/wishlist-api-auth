package com.pxdkxvan.auth.aspect.service;

import com.pxdkxvan.auth.aspect.AspectPriority;
import com.pxdkxvan.auth.logger.aspect.DefaultAspectLogger;
import com.pxdkxvan.auth.logger.LoggerArgs;
import com.pxdkxvan.auth.logger.LoggerBuilder;
import com.pxdkxvan.auth.model.Authorization;

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

    private static final LoggerBuilder.Prefix PREFIX = LoggerBuilder.Prefix.SERVICE;

    private static final String AUTHORIZATION_KEY = LoggerArgs.AUTHORIZATION_KEY;

    private static final String AFTER_AUTH_CREATE_MESSAGE = "Account authorization created";
    private static final String AFTER_AUTH_GET_OR_CREATE_MESSAGE = "Account authorization obtained";

    private final DefaultAspectLogger defaultAspectLogger;

    @AfterReturning(value = "execution(* com.pxdkxvan.auth.service.AuthorizationService.create*(..))", returning = "auth")
    void logAfterReturningAuthServiceCreateMethod(Authorization auth) {
        Map<String, Object> entries = Collections.singletonMap(AUTHORIZATION_KEY, auth);
        defaultAspectLogger.between(PREFIX, AFTER_AUTH_CREATE_MESSAGE, entries);
    }

    @AfterReturning(value = "execution(* com.pxdkxvan.auth.service.AuthorizationService.get*(..))", returning = "auth")
    void logAfterReturningAuthServiceGetOrCreateMethod(Authorization auth) {
        Map<String, Object> entries = Collections.singletonMap(AUTHORIZATION_KEY, auth);
        defaultAspectLogger.between(PREFIX, AFTER_AUTH_GET_OR_CREATE_MESSAGE, entries);
    }

}
