package com.pxdkxvan.auth.aspect.adapter;

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

import java.util.Map;

@Aspect
@Component
@Order(AspectPriority.MEDIUM)
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
final class SMTPAdapterAspect {

    private static final LoggerBuilder.Prefix PREFIX = LoggerBuilder.Prefix.ADAPTER;

    private static final String RECEIVER_KEY = LoggerArgs.RECEIVER_KEY;
    private static final String ACCOUNT_KEY = LoggerArgs.ACCOUNT_KEY;

    private static final String AFTER_EMAIL_DISPATCHER_MESSAGE = "Email sent";

    private final DefaultAspectLogger defaultAspectLogger;

    @AfterReturning(value = "execution(* com.pxdkxvan.auth.adapter.SMTPAdapter.send*(..)) && args(*, account, ..)", argNames = "account")
    void logAfterReturningEmailDispatcherSendMethod(Account account) {
        String receiver = EmailMasker.mask(account.getEmail());
        Map<String, Object> entries = Map.ofEntries(Map.entry(RECEIVER_KEY, receiver), Map.entry(ACCOUNT_KEY, account));
        defaultAspectLogger.between(PREFIX, AFTER_EMAIL_DISPATCHER_MESSAGE, entries);
    }

}
