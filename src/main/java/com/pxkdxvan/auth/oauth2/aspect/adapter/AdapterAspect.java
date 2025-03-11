package com.pxkdxvan.auth.oauth2.aspect.adapter;

import com.pxkdxvan.auth.shared.aspect.AspectPriority;
import com.pxkdxvan.auth.shared.logger.aspect.DefaultAspectLogger;

import com.pxkdxvan.auth.shared.logger.LoggerArgs;
import com.pxkdxvan.auth.shared.logger.LoggerBuilder;

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
final class AdapterAspect {

    private static final LoggerBuilder.Prefix ADAPTER_PREFIX = LoggerBuilder.Prefix.ADAPTER;

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

    @Around(value = "execution(* com.pxkdxvan.auth.oauth2.adapter.*.load*(..)) && args(request)", argNames = "joinPoint, request")
    Object logAroundAdapterLoadMethods(ProceedingJoinPoint joinPoint, OAuth2UserRequest request) throws Throwable {
        Map<String, Object> entries = Collections.singletonMap(PROTOCOL_KEY, getProtocol(request));
        defaultAspectLogger.between(ADAPTER_PREFIX, BEFORE_ADAPTER_LOAD_MESSAGE, entries);

        Object result = joinPoint.proceed();

        defaultAspectLogger.between(ADAPTER_PREFIX, AFTER_ADAPTER_LOAD_MESSAGE, null);

        return result;
    }

}
