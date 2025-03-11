package com.pxkdxvan.auth.shared.aspect.builder;

import com.pxkdxvan.auth.shared.aspect.AspectPriority;
import com.pxkdxvan.auth.shared.dto.ResponseCode;
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
final class SimpleResponseBuilderAspect {

    private static final LoggerBuilder.Prefix BUILDER_PREFIX = LoggerBuilder.Prefix.BUILDER;
    private static final String CODE_KEY = LoggerArgs.CODE_KEY;
    private static final String AFTER_SIMPLE_RESPONSE_MESSAGE = "HTTP response prepared";

    private final DefaultAspectLogger defaultAspectLogger;

    @AfterReturning(value = "execution(* com.pxkdxvan.auth.shared.builder.SimpleResponseBuilder.*(..)) && args(.., code)", argNames = "code")
    void logAfterReturningSimpleResponseBuilderMethods(ResponseCode code) {
        Map<String, Object> entries = Collections.singletonMap(CODE_KEY, code);
        defaultAspectLogger.between(BUILDER_PREFIX, AFTER_SIMPLE_RESPONSE_MESSAGE, entries);
    }

}
