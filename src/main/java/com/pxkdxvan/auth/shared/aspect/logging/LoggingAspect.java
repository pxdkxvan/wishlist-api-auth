package com.pxkdxvan.auth.shared.aspect.logging;

import com.pxkdxvan.auth.shared.logger.aspect.DefaultAspectLogger;
import com.pxkdxvan.auth.shared.logger.LoggerBuilder;
import org.aspectj.lang.ProceedingJoinPoint;

public abstract class LoggingAspect {
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
