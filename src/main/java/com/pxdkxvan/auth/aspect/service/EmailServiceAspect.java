package com.pxdkxvan.auth.aspect.service;

import com.pxdkxvan.auth.model.VerificationMethod;
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
final class EmailServiceAspect {

    private static final LoggerBuilder.Prefix PREFIX = LoggerBuilder.Prefix.SERVICE;
    private static final String VERIFICATION_KEY = LoggerArgs.VERIFICATION_KEY;
    private static final String AFTER_EMAIL_SEND_MESSAGE = "Email verification settled";

    private final DefaultAspectLogger defaultAspectLogger;

    @AfterReturning(value = "execution(* com.pxdkxvan.auth.service.MailingService.send*(..)) && args(method, ..)", argNames = "method")
    void logAfterReturningEmailServiceSendMethod(VerificationMethod method) {
        Map<String, Object> entries = Collections.singletonMap(VERIFICATION_KEY, method);
        defaultAspectLogger.between(PREFIX, AFTER_EMAIL_SEND_MESSAGE, entries);
    }

}
