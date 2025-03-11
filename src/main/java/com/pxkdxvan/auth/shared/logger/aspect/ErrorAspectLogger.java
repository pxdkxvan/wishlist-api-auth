package com.pxkdxvan.auth.shared.logger.aspect;

import com.pxkdxvan.auth.shared.logger.LoggerBuilder;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
@Component
public final class ErrorAspectLogger extends DefaultAspectLogger {

    private String logOnThrowing(LoggerBuilder.Prefix prefix, Map<String, Object> entries) {
        return this
                .logHeader(prefix)
                .asThrown()
                .withEntries(entries)
                .withId()
                .build();
    }

    public void after(LoggerBuilder.Prefix prefix, Map<String, Object> entries) {
        log.warn(logOnThrowing(prefix, entries));
    }

    public void after(LoggerBuilder.Prefix prefix, Map<String, Object> entries, Throwable thr) {
        log.warn(logOnThrowing(prefix, entries), thr);
    }


}
