package com.pxkdxvan.auth.local.aspect.service;

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
@Order(AspectPriority.LOWEST)
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
final class TemplateServiceAspect {

    private static final LoggerBuilder.Prefix SERVICE_PREFIX = LoggerBuilder.Prefix.SERVICE;
    private static final String ACCOUNT_KEY = LoggerArgs.ACCOUNT_KEY;
    private static final String AFTER_TEMPLATE_GENERATE_MESSAGE = "Thymeleaf template translated in HTML";

    private final DefaultAspectLogger defaultAspectLogger;

    @AfterReturning(value = "execution(* com.pxkdxvan.auth.local.service.TemplateService.generate*(..)) && args(*, account, ..)", argNames = "account")
    void lofAfterReturningTemplateServiceGenerateMethod(Account account) {
        Map<String, Object> entries = Collections.singletonMap(ACCOUNT_KEY, account);
        defaultAspectLogger.between(SERVICE_PREFIX, AFTER_TEMPLATE_GENERATE_MESSAGE, entries);
    }

}
