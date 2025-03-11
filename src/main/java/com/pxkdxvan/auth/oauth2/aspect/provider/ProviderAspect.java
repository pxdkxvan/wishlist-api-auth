package com.pxkdxvan.auth.oauth2.aspect.provider;

import com.pxkdxvan.auth.oauth2.provider.DefaultOAuth2Provider;
import com.pxkdxvan.auth.shared.aspect.AspectPriority;
import com.pxkdxvan.auth.shared.logger.aspect.DefaultAspectLogger;
import com.pxkdxvan.auth.shared.logger.LoggerArgs;
import com.pxkdxvan.auth.shared.logger.LoggerBuilder;
import com.pxkdxvan.auth.shared.model.Provider;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

import org.aspectj.lang.JoinPoint;
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
final class ProviderAspect {

    private static final LoggerBuilder.Prefix PROVIDER_PREFIX = LoggerBuilder.Prefix.PROVIDER;
    private static final String PROVIDER_KEY = LoggerArgs.PROVIDER_KEY;
    private static final String AFTER_PROVIDER_GET_MESSAGE = "OAuth2 account data loaded";

    private final DefaultAspectLogger defaultAspectLogger;

    @AfterReturning(value = "execution(* com.pxkdxvan.auth.oauth2.provider..getAccountData(..))")
    void logAfterReturningProviderGetAccountDataMethods(JoinPoint joinPoint) {
        Provider provider = ((DefaultOAuth2Provider) joinPoint.getTarget()).getProvider();
        Map<String, Object> entries = Collections.singletonMap(PROVIDER_KEY, provider);
        defaultAspectLogger.between(PROVIDER_PREFIX, AFTER_PROVIDER_GET_MESSAGE, entries);
    }

}

