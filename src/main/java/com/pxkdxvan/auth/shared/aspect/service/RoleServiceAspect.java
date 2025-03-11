package com.pxkdxvan.auth.shared.aspect.service;

import com.pxkdxvan.auth.shared.aspect.AspectPriority;
import com.pxkdxvan.auth.shared.logger.aspect.DefaultAspectLogger;
import com.pxkdxvan.auth.shared.logger.LoggerArgs;
import com.pxkdxvan.auth.shared.logger.LoggerBuilder;
import com.pxkdxvan.auth.shared.model.Role;

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

    private static final LoggerBuilder.Prefix SERVICE_PREFIX = LoggerBuilder.Prefix.SERVICE;
    private static final String ROLE_KEY = LoggerArgs.ROLE_KEY;
    private static final String AFTER_ROLE_GET_MESSAGE = "Received default role";

    private final DefaultAspectLogger defaultAspectLogger;

    @AfterReturning(value = "execution(* com.pxkdxvan.auth.shared.service.RoleService.get*(..))", returning = "role")
    void logAfterReturningRoleServiceGetMethod(Role role) {
        Map<String, Object> entries = Collections.singletonMap(ROLE_KEY, role);
        defaultAspectLogger.between(SERVICE_PREFIX, AFTER_ROLE_GET_MESSAGE, entries);
    }

}
