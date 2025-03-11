package com.pxkdxvan.auth.shared.aspect.builder;

import com.pxkdxvan.auth.shared.aspect.AspectPriority;
import com.pxkdxvan.auth.shared.logger.aspect.DefaultAspectLogger;
import com.pxkdxvan.auth.shared.logger.LoggerArgs;
import com.pxkdxvan.auth.shared.logger.LoggerBuilder;

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

    private static final LoggerBuilder.Prefix BUILDER_PREFIX = LoggerBuilder.Prefix.BUILDER;
    private static final String SUBJECT_KEY = LoggerArgs.SUBJECT_KEY;
    private static final String AFTER_TOKEN_RESPONSE_MESSAGE = "Token response prepared";

    private final DefaultAspectLogger defaultAspectLogger;

    @AfterReturning(value = "execution(* com.pxkdxvan.auth.shared.builder.TokenResponseBuilder.*(..)) && args(.., subject)", argNames = "subject")
    void logAfterReturningTokenResponseBuilderMethods(String subject) {
        Map<String, Object> entries = Collections.singletonMap(SUBJECT_KEY, subject);
        defaultAspectLogger.between(BUILDER_PREFIX, AFTER_TOKEN_RESPONSE_MESSAGE, entries);
    }

}
