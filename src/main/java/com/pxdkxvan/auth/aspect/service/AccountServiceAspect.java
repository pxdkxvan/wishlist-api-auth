package com.pxdkxvan.auth.aspect.service;

import com.pxdkxvan.auth.util.EmailMasker;
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

import java.util.Collections;
import java.util.Map;

@Aspect
@Component
@Order(AspectPriority.LOW)
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
final class AccountServiceAspect {

    private static final LoggerBuilder.Prefix PREFIX = LoggerBuilder.Prefix.SERVICE;

    private static final String ACCOUNT_KEY = LoggerArgs.ACCOUNT_KEY;

    private static final String AFTER_ACCOUNT_TAKEN_MESSAGE = "Account existence checked";
    private static final String AFTER_ACCOUNT_FIND_MESSAGE = "Account found by id";
    private static final String AFTER_ACCOUNT_CREATE_MESSAGE = "Account created";
    private static final String AFTER_ACCOUNT_GET_OR_CREATE_MESSAGE = "Account obtained";
    private static final String AFTER_ACCOUNT_VERIFY_MESSAGE = "Account verified";

    private final DefaultAspectLogger defaultAspectLogger;

    @AfterReturning(value = "execution(* com.pxdkxvan.auth.service.AccountService.is*(..)) && args(login)", argNames = "login")
    void lofAfterReturningAccountServiceTakenMethod(String login) {
        Map<String, Object> entries = Collections.singletonMap(ACCOUNT_KEY, EmailMasker.mask(login));
        defaultAspectLogger.between(PREFIX, AFTER_ACCOUNT_TAKEN_MESSAGE, entries);
    }

    @AfterReturning(value = "execution(* com.pxdkxvan.auth.service.AccountService.find*(..))", returning = "account")
    void logAfterReturningAccountServiceFindMethod(Account account) {
        Map<String, Object> entries = Collections.singletonMap(ACCOUNT_KEY, account);
        defaultAspectLogger.between(PREFIX, AFTER_ACCOUNT_FIND_MESSAGE, entries);
    }

    @AfterReturning(value = "execution(* com.pxdkxvan.auth.service.AccountService.create*(..))", returning = "account")
    void logAfterReturningAccountServiceCreateMethod(Account account) {
        Map<String, Object> entries = Collections.singletonMap(ACCOUNT_KEY, account);
        defaultAspectLogger.between(PREFIX, AFTER_ACCOUNT_CREATE_MESSAGE, entries);
    }

    @AfterReturning(value = "execution(* com.pxdkxvan.auth.service.AccountService.get*(..))", returning = "account")
    void logAfterReturningAccountServiceGetOrCreateMethod(Account account) {
        Map<String, Object> entries = Collections.singletonMap(ACCOUNT_KEY, account);
        defaultAspectLogger.between(PREFIX, AFTER_ACCOUNT_GET_OR_CREATE_MESSAGE, entries);
    }

    @AfterReturning(value = "execution(* com.pxdkxvan.auth.service.AccountService.verify*(..))", returning = "account")
    void logAfterReturningAccountServiceVerifyMethod(Account account) {
        Map<String, Object> entries = Collections.singletonMap(ACCOUNT_KEY, account);
        defaultAspectLogger.between(PREFIX, AFTER_ACCOUNT_VERIFY_MESSAGE, entries);
    }

}
