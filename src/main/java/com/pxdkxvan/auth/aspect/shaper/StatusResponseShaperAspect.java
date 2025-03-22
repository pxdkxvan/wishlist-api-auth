package com.pxdkxvan.auth.aspect.shaper;

import com.pxdkxvan.auth.aspect.AspectPriority;
import com.pxdkxvan.auth.response.ResponseCode;
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
final class StatusResponseShaperAspect {

    private static final LoggerBuilder.Prefix PREFIX = LoggerBuilder.Prefix.SHAPER;
    private static final String CODE_KEY = LoggerArgs.CODE_KEY;
    private static final String AFTER_SIMPLE_RESPONSE_MESSAGE = "HTTP response prepared";

    private final DefaultAspectLogger defaultAspectLogger;

    @AfterReturning(value = "execution(* com.pxdkxvan.auth.response.StatusResponseShaper.*(..)) && args(.., code)", argNames = "code")
    void logAfterReturningStatusResponseBuilderMethods(ResponseCode code) {;
        Map<String, Object> entries = Collections.singletonMap(CODE_KEY, code);
        defaultAspectLogger.between(PREFIX, AFTER_SIMPLE_RESPONSE_MESSAGE, entries);
    }

}
