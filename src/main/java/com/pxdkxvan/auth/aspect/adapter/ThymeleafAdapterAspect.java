package com.pxdkxvan.auth.aspect.adapter;

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
@Order(AspectPriority.LOWEST)
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
final class ThymeleafAdapterAspect {

    private static final LoggerBuilder.Prefix PREFIX = LoggerBuilder.Prefix.ADAPTER;
    private static final String ACCOUNT_KEY = LoggerArgs.ACCOUNT_KEY;
    private static final String AFTER_TEMPLATE_GENERATE_MESSAGE = "Thymeleaf template translated in HTML";

    private final DefaultAspectLogger defaultAspectLogger;

    @AfterReturning(value = "execution(* com.pxdkxvan.auth.adapter.ThymeleafAdapter.generate*(..)) && args(*, account, ..)", argNames = "account")
    void lofAfterReturningTemplateServiceGenerateMethod(Account account) {
        Map<String, Object> entries = Collections.singletonMap(ACCOUNT_KEY, account);
        defaultAspectLogger.between(PREFIX, AFTER_TEMPLATE_GENERATE_MESSAGE, entries);
    }

}
