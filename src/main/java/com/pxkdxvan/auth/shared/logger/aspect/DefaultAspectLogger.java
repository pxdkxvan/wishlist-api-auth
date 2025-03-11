package com.pxkdxvan.auth.shared.logger.aspect;

import com.pxkdxvan.auth.shared.logger.LoggerBuilder;
import lombok.extern.slf4j.Slf4j;

import org.aspectj.lang.JoinPoint;

import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
@Component
public class DefaultAspectLogger {

    protected LoggerBuilder.PrefixedHeaderStep logHeader(LoggerBuilder.Prefix prefix) {
        return LoggerBuilder
                .builder()
                .withPrefix(prefix);
    }

    protected String logOnStarting(LoggerBuilder.Prefix prefix, String className, String methodName) {
        return this
                .logHeader(prefix)
                .asStarted()
                .withHandler(className, methodName)
                .withId()
                .build();
    }

    protected String logOnGoing(LoggerBuilder.Prefix prefix, String caption, Map<String, Object> entries) {
        return this
                .logHeader(prefix)
                .asOngoing()
                .withId()
                .withCaption(caption)
                .withEntries(entries)
                .build();
    }

    protected String logOnThrowing(LoggerBuilder.Prefix prefix, String errorClassName, String errorMessage) {
        return this
                .logHeader(prefix)
                .asThrown()
                .withException(errorClassName, errorMessage)
                .withId()
                .build();
    }

    protected String logOnCompleting(LoggerBuilder.Prefix prefix, Long exec) {
        return this
                .logHeader(prefix)
                .asCompleted()
                .withExecution(exec)
                .withId()
                .build();
    }

    public void before(LoggerBuilder.Prefix prefix, JoinPoint joinPoint) {
        String className = joinPoint.getSignature().getDeclaringTypeName();
        String methodName = joinPoint.getSignature().getName();
        String message = logOnStarting(prefix, className, methodName);
        log.debug(message);
    }

    public void between(LoggerBuilder.Prefix prefix, String caption, Map<String, Object> entries) {
        String message = logOnGoing(prefix, caption, entries);
        log.info(message);
    }

    public void after(LoggerBuilder.Prefix prefix, Throwable throwable) {
        String errorClassName = throwable.getClass().getName();
        String errorMessage = throwable.getMessage();
        String message = logOnThrowing(prefix, errorClassName, errorMessage);
        log.warn(message);
    }

    public void after(LoggerBuilder.Prefix prefix, Long execution) {
        String message = logOnCompleting(prefix, execution);
        log.debug(message);
    }

}
