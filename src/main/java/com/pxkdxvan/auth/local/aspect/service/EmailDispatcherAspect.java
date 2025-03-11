package com.pxkdxvan.auth.local.aspect.service;

import com.pxkdxvan.auth.local.utils.EmailMasker;
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

import java.util.Map;

@Aspect
@Component
@Order(AspectPriority.MEDIUM)
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
final class EmailDispatcherAspect {

    private static final LoggerBuilder.Prefix SERVICE_PREFIX = LoggerBuilder.Prefix.SERVICE;

    private static final String RECEIVER_KEY = LoggerArgs.RECEIVER_KEY;
    private static final String ACCOUNT_KEY = LoggerArgs.ACCOUNT_KEY;

    private static final String AFTER_EMAIL_DISPATCHER_MESSAGE = "Email sent";

    private final DefaultAspectLogger defaultAspectLogger;

    @AfterReturning(value = "execution(* com.pxkdxvan.auth.local.service.EmailDispatcher.send*(..)) && args(*, account, ..)", argNames = "account")
    void logAfterReturningEmailDispatcherSendMethod(Account account) {
        String receiver = EmailMasker.mask(account.getEmail());
        Map<String, Object> entries = Map.ofEntries(Map.entry(RECEIVER_KEY, receiver), Map.entry(ACCOUNT_KEY, account));
        defaultAspectLogger.between(SERVICE_PREFIX, AFTER_EMAIL_DISPATCHER_MESSAGE, entries);
    }

}
