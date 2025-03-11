package com.pxkdxvan.auth.oauth2.aspect.controller;

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
@Order(AspectPriority.MEDIUM)
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
final class OAuth2ControllerAspect {

    private static final LoggerBuilder.Prefix CONTROLLER_PREFIX = LoggerBuilder.Prefix.CONTROLLER;
    private static final String ACCOUNT_KEY = LoggerArgs.ACCOUNT_KEY;
    private static final String AFTER_OAUTH2_CALLBACK = "Logged in via OAuth2";

    private final DefaultAspectLogger defaultAspectLogger;

    @AfterReturning(value = "execution(* com.pxkdxvan.auth.oauth2.controller.OAuth2LoginController.callback(..)) && args(user)", argNames = "user")
    void logAfterReturningOAuth2ControllerCallbackMethod(OAuth2User user) {
        String accountId = user.getAttribute(ConstantsConfig.ACCOUNT_ID_ATTR_NAME);
        Map<String, Object> entries = Collections.singletonMap(ACCOUNT_KEY, accountId);
        defaultAspectLogger.between(CONTROLLER_PREFIX, AFTER_OAUTH2_CALLBACK, entries);
    }

}
