package com.pxkdxvan.auth.shared.thread;

import io.micrometer.common.lang.NonNullApi;

import org.slf4j.MDC;
import org.springframework.core.task.TaskDecorator;

import java.util.Map;

@NonNullApi
public final class MDCDecorator implements TaskDecorator {
    @Override
    public Runnable decorate(Runnable runnable) {
        Map<String, String> mdcContext = MDC.getCopyOfContextMap();
        return () -> {
            if (mdcContext != null)MDC.setContextMap(mdcContext);
            try {
                runnable.run();
            } finally {
                MDC.clear();
            }
        };
    }
}
