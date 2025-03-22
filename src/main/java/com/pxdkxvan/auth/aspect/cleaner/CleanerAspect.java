package com.pxdkxvan.auth.aspect.cleaner;

import com.pxdkxvan.auth.aspect.AspectPriority;

import com.pxdkxvan.auth.logger.aspect.UnsignedAspectLogger;
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
@Order(AspectPriority.HIGHEST)
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
final class CleanerAspect {

    private static final LoggerBuilder.Prefix PREFIX = LoggerBuilder.Prefix.CLEANER;

    private static final String AMOUNT_KEY = LoggerArgs.AMOUNT_KEY;

    private static final String AFTER_ACCOUNT_CLEAN_MESSAGE = "Unverified accounts cleaned";
    private static final String AFTER_VERIFICATION_CLEAN_MESSAGE = "Unused verification records cleaned";

    private final UnsignedAspectLogger unsignedAspectLogger;

    @AfterReturning(value = "execution(* com.pxdkxvan.auth.cleaner.AccountCleaner.clean*(..))", returning = "records")
    void logAfterReturningAccountCleanerCleanMethod(int records) {
        Map<String, Object> entries = Collections.singletonMap(AMOUNT_KEY, records);
        unsignedAspectLogger.between(PREFIX, AFTER_ACCOUNT_CLEAN_MESSAGE, entries);
    }

    @AfterReturning(value = "execution(* com.pxdkxvan.auth.cleaner.VerificationCleaner.clean*(..))", returning = "records")
    void logAfterReturningVerificationCleanerCleanMethod(int records) {
        Map<String, Object> entries = Collections.singletonMap(AMOUNT_KEY, records);
        unsignedAspectLogger.between(PREFIX, AFTER_VERIFICATION_CLEAN_MESSAGE, entries);
    }

}
