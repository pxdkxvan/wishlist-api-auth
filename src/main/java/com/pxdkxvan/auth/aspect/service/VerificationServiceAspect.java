package com.pxdkxvan.auth.aspect.service;

import com.pxdkxvan.auth.model.Verification;
import com.pxdkxvan.auth.model.VerificationMethod;
import com.pxdkxvan.auth.aspect.AspectPriority;
import com.pxdkxvan.auth.logger.aspect.DefaultAspectLogger;
import com.pxdkxvan.auth.logger.LoggerArgs;
import com.pxdkxvan.auth.logger.LoggerBuilder;
import com.pxdkxvan.auth.model.Account;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Map;

@Aspect
@Component
@Order(AspectPriority.MEDIUM)
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
final class VerificationServiceAspect {

    private static final LoggerBuilder.Prefix PREFIX = LoggerBuilder.Prefix.SERVICE;

    private static final String VERIFICATION_KEY = LoggerArgs.VERIFICATION_KEY;
    private static final String ACCOUNT_KEY = LoggerArgs.ACCOUNT_KEY;

    private static final String AFTER_VERIFICATION_GENERATE_MESSAGE = "Email verification key stored";
    private static final String AFTER_VERIFICATION_VERIFY_MESSAGE = "Email verification key deleted due to account verification";

    private final DefaultAspectLogger defaultAspectLogger;

    @AfterReturning(value = "execution(* com.pxdkxvan.auth.service.VerificationService.generate*(method, account, ..))", argNames = "method, account")
    void logAfterReturningVerificationServiceGenerateMethod(VerificationMethod method, Account account) {
        Map<String, Object> entries = Map.ofEntries(Map.entry(VERIFICATION_KEY, method), Map.entry(ACCOUNT_KEY, account));
        defaultAspectLogger.between(PREFIX, AFTER_VERIFICATION_GENERATE_MESSAGE, entries);
    }

    @AfterReturning(value = "execution(* com.pxdkxvan.auth.service.VerificationService.verify*(verification, ..))", argNames = "verification")
    void logAfterReturningVerificationServiceVerifyMethod(Verification verification) {
        VerificationMethod method = verification.getMethod();
        Account account = verification.getAccount();
        Map<String, Object> entries = Map.ofEntries(Map.entry(VERIFICATION_KEY, method), Map.entry(ACCOUNT_KEY, account));
        defaultAspectLogger.between(PREFIX, AFTER_VERIFICATION_VERIFY_MESSAGE, entries);
    }

}
