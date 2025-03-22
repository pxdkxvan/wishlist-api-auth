package com.pxdkxvan.auth.aspect.controller;

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
@Order(AspectPriority.MEDIUM)
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
final class OAuth2ControllerAspect {

    private static final LoggerBuilder.Prefix PREFIX = LoggerBuilder.Prefix.CONTROLLER;
    private static final String ACCOUNT_KEY = LoggerArgs.ACCOUNT_KEY;
    private static final String AFTER_OAUTH2_CALLBACK = "Logged in via OAuth2";

    private final DefaultAspectLogger defaultAspectLogger;

    @AfterReturning(value = "execution(* com.pxdkxvan.auth.controller.OAuth2AuthController.callback(..)) && args(user)", argNames = "user")
    void logAfterReturningOAuth2ControllerCallbackMethod(OAuth2User user) {
        UUID accountId = user.getAttribute(ConstantsConfig.ACCOUNT_ID_ATTR_NAME);
        Map<String, Object> entries = Collections.singletonMap(ACCOUNT_KEY, accountId);
        defaultAspectLogger.between(PREFIX, AFTER_OAUTH2_CALLBACK, entries);
    }

}
