package com.pxdkxvan.auth.aspect.service;

import com.pxdkxvan.auth.aspect.AspectPriority;
import com.pxdkxvan.auth.logger.aspect.DefaultAspectLogger;
import com.pxdkxvan.auth.logger.LoggerArgs;
import com.pxdkxvan.auth.logger.LoggerBuilder;
import com.pxdkxvan.auth.model.Role;

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
final class RoleServiceAspect {

    private static final LoggerBuilder.Prefix PREFIX = LoggerBuilder.Prefix.SERVICE;
    private static final String ROLE_KEY = LoggerArgs.ROLE_KEY;
    private static final String AFTER_ROLE_GET_MESSAGE = "Received default role";

    private final DefaultAspectLogger defaultAspectLogger;

    @AfterReturning(value = "execution(* com.pxdkxvan.auth.service.RoleService.get*(..))", returning = "role")
    void logAfterReturningRoleServiceGetMethod(Role role) {
        Map<String, Object> entries = Collections.singletonMap(ROLE_KEY, role);
        defaultAspectLogger.between(PREFIX, AFTER_ROLE_GET_MESSAGE, entries);
    }

}
