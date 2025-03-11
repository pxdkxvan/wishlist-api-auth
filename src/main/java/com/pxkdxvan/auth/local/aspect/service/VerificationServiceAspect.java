package com.pxkdxvan.auth.local.aspect.service;

import com.pxkdxvan.auth.local.model.VerificationMethod;
import com.pxkdxvan.auth.shared.aspect.AspectPriority;
import com.pxkdxvan.auth.shared.logger.aspect.DefaultAspectLogger;
import com.pxkdxvan.auth.shared.logger.LoggerArgs;
import com.pxkdxvan.auth.shared.logger.LoggerBuilder;
import com.pxkdxvan.auth.shared.model.Account;

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
final class VerificationServiceAspect {

    private static final LoggerBuilder.Prefix SERVICE_PREFIX = LoggerBuilder.Prefix.SERVICE;

    private static final String VERIFICATION_KEY = LoggerArgs.VERIFICATION_KEY;
    private static final String ACCOUNT_KEY = LoggerArgs.ACCOUNT_KEY;

    private static final String AFTER_VERIFICATION_GENERATE_MESSAGE = "Email verification key stored";
    private static final String AFTER_VERIFICATION_VERIFY_MESSAGE = "Email verification key deleted due to account verification";

    private final DefaultAspectLogger defaultAspectLogger;

    @AfterReturning(value = "execution(* com.pxkdxvan.auth.local.service.VerificationService.generate*(method, account, ..))", argNames = "method, account")
    void logAfterReturningVerificationServiceGenerateMethod(VerificationMethod method, Account account) {
        Map<String, Object> entries = Map.ofEntries(Map.entry(VERIFICATION_KEY, method), Map.entry(ACCOUNT_KEY, account));
        defaultAspectLogger.between(SERVICE_PREFIX, AFTER_VERIFICATION_GENERATE_MESSAGE, entries);
    }

    @AfterReturning(value = "execution(* com.pxkdxvan.auth.local.service.VerificationService.verify*(account, ..))", argNames = "account")
    void logAfterReturningVerificationServiceVerifyMethod(Account account) {
        Map<String, Object> entries = Collections.singletonMap(ACCOUNT_KEY, account);
        defaultAspectLogger.between(SERVICE_PREFIX, AFTER_VERIFICATION_VERIFY_MESSAGE, entries);
    }

}
