package com.pxkdxvan.auth.oauth2.aspect.service;

import com.pxkdxvan.auth.shared.aspect.AspectPriority;
import com.pxkdxvan.auth.shared.config.ConstantsConfig;
import com.pxkdxvan.auth.shared.logger.aspect.DefaultAspectLogger;
import com.pxkdxvan.auth.shared.logger.LoggerArgs;
import com.pxkdxvan.auth.shared.logger.LoggerBuilder;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;

import org.springframework.core.annotation.Order;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Map;

@Aspect
@Component
@Order(AspectPriority.LOW)
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public class OAuth2ServiceAspect {

    private static final LoggerBuilder.Prefix SERVICE_PREFIX = LoggerBuilder.Prefix.SERVICE;
    private static final String ACCOUNT_KEY = LoggerArgs.ACCOUNT_KEY;
    private static final String AFTER_OAUTH2_LOAD_MESSAGE = "OAuth2 client initialized";

    private final DefaultAspectLogger defaultAspectLogger;

    @AfterReturning(value = "execution(* com.pxkdxvan.auth.oauth2.service.OAuth2LoginService.load*(..))", returning = "user")
    void logAfterReturningOAuth2ServiceLoadMethod(OAuth2User user) {
        String accountId = user.getAttribute(ConstantsConfig.ACCOUNT_ID_ATTR_NAME);
        Map<String, Object> entries = Collections.singletonMap(ACCOUNT_KEY, accountId);
        defaultAspectLogger.between(SERVICE_PREFIX, AFTER_OAUTH2_LOAD_MESSAGE, entries);
    }

}
