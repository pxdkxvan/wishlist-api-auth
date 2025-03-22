package com.pxdkxvan.auth.aspect.shaper;

import com.pxdkxvan.auth.aspect.AspectPriority;
import com.pxdkxvan.auth.logger.aspect.DefaultAspectLogger;
import com.pxdkxvan.auth.logger.LoggerArgs;
import com.pxdkxvan.auth.logger.LoggerBuilder;

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
@Order(AspectPriority.MEDIUM)
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
final class TokenResponseBuilderAspect {

    private static final LoggerBuilder.Prefix PREFIX = LoggerBuilder.Prefix.SHAPER;
    private static final String SUBJECT_KEY = LoggerArgs.SUBJECT_KEY;
    private static final String AFTER_TOKEN_RESPONSE_MESSAGE = "Token response prepared";

    private final DefaultAspectLogger defaultAspectLogger;

    @AfterReturning(value = "execution(* com.pxdkxvan.auth.response.TokenResponseShaper.*(..)) && args(.., subject)", argNames = "subject")
    void logAfterReturningTokenResponseBuilderMethods(String subject) {
        Map<String, Object> entries = Collections.singletonMap(SUBJECT_KEY, subject);
        defaultAspectLogger.between(PREFIX, AFTER_TOKEN_RESPONSE_MESSAGE, entries);
    }

}
