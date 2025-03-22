package com.pxdkxvan.auth.aspect.core;

import com.pxdkxvan.auth.logger.aspect.DefaultAspectLogger;
import com.pxdkxvan.auth.logger.LoggerBuilder;
import org.aspectj.lang.ProceedingJoinPoint;

public abstract sealed class LoggingAspect permits ControllerAspect, ServiceAspect {
    protected final Object around(ProceedingJoinPoint joinPoint, DefaultAspectLogger aspectLogger,
                            LoggerBuilder.Prefix beforePrefix, LoggerBuilder.Prefix afterPrefix) throws Throwable {
        aspectLogger.before(beforePrefix, joinPoint);
        try {
            long start = System.currentTimeMillis();
            Object result = joinPoint.proceed();
            long end = System.currentTimeMillis();

            aspectLogger.after(afterPrefix, end - start);

            return result;
        } catch (Throwable thr) {
            aspectLogger.after(afterPrefix, thr);
            throw thr;
        }
    }
}
