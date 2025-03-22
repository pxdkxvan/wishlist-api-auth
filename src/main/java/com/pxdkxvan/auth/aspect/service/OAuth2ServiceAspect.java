package com.pxdkxvan.auth.aspect.service;

import com.pxdkxvan.auth.aspect.AspectPriority;
import com.pxdkxvan.auth.config.ConstantsConfig;
import com.pxdkxvan.auth.logger.aspect.DefaultAspectLogger;
import com.pxdkxvan.auth.logger.LoggerArgs;
import com.pxdkxvan.auth.logger.LoggerBuilder;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;

import org.springframework.core.annotation.Order;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Map;
import java.util.UUID;

@Aspect
@Component
@Order(AspectPriority.LOW)
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public final class OAuth2ServiceAspect {

    private static final LoggerBuilder.Prefix PREFIX = LoggerBuilder.Prefix.SERVICE;
    private static final String ACCOUNT_KEY = LoggerArgs.ACCOUNT_KEY;
    private static final String AFTER_OAUTH2_LOAD_MESSAGE = "OAuth2 client initialized";

    private final DefaultAspectLogger defaultAspectLogger;

    @AfterReturning(value = "execution(* com.pxdkxvan.auth.service.OAuth2LoginService.load*(..))", returning = "user")
    void logAfterReturningOAuth2ServiceLoadMethod(OAuth2User user) {
        UUID accountId = user.getAttribute(ConstantsConfig.ACCOUNT_ID_ATTR_NAME);
        Map<String, Object> entries = Collections.singletonMap(ACCOUNT_KEY, accountId);
        defaultAspectLogger.between(PREFIX, AFTER_OAUTH2_LOAD_MESSAGE, entries);
    }

}
