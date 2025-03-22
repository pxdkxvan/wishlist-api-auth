package com.pxdkxvan.auth.aspect.adapter;

import com.pxdkxvan.auth.aspect.AspectPriority;
import com.pxdkxvan.auth.logger.aspect.DefaultAspectLogger;

import com.pxdkxvan.auth.logger.LoggerArgs;
import com.pxdkxvan.auth.logger.LoggerBuilder;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

import org.springframework.core.annotation.Order;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Map;

@Aspect
@Component
@Order(AspectPriority.LOW)
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
final class OAuth2AdapterAspect {

    private static final LoggerBuilder.Prefix PREFIX = LoggerBuilder.Prefix.ADAPTER;

    private static final String PROTOCOL_KEY = LoggerArgs.PROTOCOL_KEY;

    private static final String BEFORE_ADAPTER_LOAD_MESSAGE = "OAuth2 request received";
    private static final String AFTER_ADAPTER_LOAD_MESSAGE = "OAuth2 user loaded";

    private final DefaultAspectLogger defaultAspectLogger;

    private enum Protocol {
        OIDC,
        OAUTH2
    }

    private Protocol getProtocol(OAuth2UserRequest request) {
        return switch (request) {
            case OidcUserRequest ignored -> Protocol.OIDC;
            case OAuth2UserRequest ignored -> Protocol.OAUTH2;
        };
    }

    @Around(value = "execution(* com.pxdkxvan.auth.adapter.*.load*(..)) && args(request)", argNames = "joinPoint, request")
    Object logAroundAdapterLoadMethods(ProceedingJoinPoint joinPoint, OAuth2UserRequest request) throws Throwable {
        Map<String, Object> entries = Collections.singletonMap(PROTOCOL_KEY, getProtocol(request));
        defaultAspectLogger.between(PREFIX, BEFORE_ADAPTER_LOAD_MESSAGE, entries);

        Object result = joinPoint.proceed();

        defaultAspectLogger.between(PREFIX, AFTER_ADAPTER_LOAD_MESSAGE, null);

        return result;
    }

}
