package com.pxdkxvan.auth.logger.aspect;

import com.pxdkxvan.auth.logger.LoggerBuilder;

import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public final class UnsignedAspectLogger extends DefaultAspectLogger {

    @Override
    protected String logOnStarting(LoggerBuilder.Prefix prefix, String className, String methodName) {
        return this
                .logHeader(prefix)
                .asStarted()
                .withHandler(className, methodName)
                .build();
    }

    @Override
    protected String logOnGoing(LoggerBuilder.Prefix prefix, String caption, Map<String, Object> entries) {
        return this
                .logHeader(prefix)
                .asOngoing()
                .withCaption(caption)
                .withEntries(entries)
                .build();
    }

    @Override
    protected String logOnThrowing(LoggerBuilder.Prefix prefix, String errorClassName, String errorMessage) {
        return this
                .logHeader(prefix)
                .asThrown()
                .withException(errorClassName, errorMessage)
                .build();
    }

    @Override
    protected String logOnCompleting(LoggerBuilder.Prefix prefix, Long exec) {
        return this
                .logHeader(prefix)
                .asCompleted()
                .withExecution(exec)
                .build();
    }

}
