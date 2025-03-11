package com.pxkdxvan.auth.shared.aspect.logging;

import com.pxkdxvan.auth.shared.aspect.AspectPriority;
import com.pxkdxvan.auth.shared.logger.aspect.DefaultAspectLogger;
import com.pxkdxvan.auth.shared.logger.LoggerBuilder;

import com.pxkdxvan.auth.shared.logger.aspect.UnsignedAspectLogger;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Order(AspectPriority.HIGH)
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
final class ServiceLoggingAspect extends LoggingAspect {

    private static final String ADAPTER_PATH = ".adapter";
    private static final String BUILDER_PATH = ".builder";
    private static final String PROVIDER_PATH = ".provider";
    private static final String CLEANER_PATH = ".cleaner";

    private static final LoggerBuilder.Prefix SERVICE_PREFIX = LoggerBuilder.Prefix.SERVICE;
    private static final LoggerBuilder.Prefix ADAPTER_PREFIX = LoggerBuilder.Prefix.ADAPTER;
    private static final LoggerBuilder.Prefix BUILDER_PREFIX = LoggerBuilder.Prefix.BUILDER;
    private static final LoggerBuilder.Prefix PROVIDER_PREFIX = LoggerBuilder.Prefix.PROVIDER;
    private static final LoggerBuilder.Prefix CLEANER_PREFIX = LoggerBuilder.Prefix.CLEANER;

    private final DefaultAspectLogger defaultAspectLogger;
    private final UnsignedAspectLogger unsignedAspectLogger;

    private LoggerBuilder.Prefix determinePrefix(String className) {
        return className.contains(ADAPTER_PATH) ? ADAPTER_PREFIX :
                className.contains(BUILDER_PATH) ? BUILDER_PREFIX :
                        className.contains(PROVIDER_PATH) ? PROVIDER_PREFIX :
                                className.contains(CLEANER_PATH) ? CLEANER_PREFIX :SERVICE_PREFIX;
    }

    private DefaultAspectLogger determineAspectLogger(LoggerBuilder.Prefix prefix) {
        return (prefix != CLEANER_PREFIX) ? defaultAspectLogger : unsignedAspectLogger;
    }

    @Around("!execution(protected * *(..)) && (@within(org.springframework.stereotype.Service) || within(com.pxkdxvan.auth.oauth2.provider.*))")
    Object logAroundServiceMethods(ProceedingJoinPoint joinPoint) throws Throwable {
        LoggerBuilder.Prefix prefix = determinePrefix(joinPoint.getSignature().getDeclaringTypeName());
        DefaultAspectLogger aspectLogger = determineAspectLogger(prefix);
        return around(joinPoint, aspectLogger, prefix, prefix);
    }

}
