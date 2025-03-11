package com.pxkdxvan.auth.local.aspect.service;

import com.pxkdxvan.auth.local.model.VerificationMethod;
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
final class EmailServiceAspect {

    private static final LoggerBuilder.Prefix SERVICE_PREFIX = LoggerBuilder.Prefix.SERVICE;
    private static final String VERIFICATION_KEY = LoggerArgs.VERIFICATION_KEY;
    private static final String AFTER_EMAIL_SEND_MESSAGE = "Email verification settled";

    private final DefaultAspectLogger defaultAspectLogger;

    @AfterReturning(value = "execution(* com.pxkdxvan.auth.local.service.EmailService.send*(..)) && args(method, ..)", argNames = "method")
    void logAfterReturningEmailServiceSendMethod(VerificationMethod method) {
        Map<String, Object> entries = Collections.singletonMap(VERIFICATION_KEY, method);
        defaultAspectLogger.between(SERVICE_PREFIX, AFTER_EMAIL_SEND_MESSAGE, entries);
    }

}
