package com.pxkdxvan.auth.shared.aspect.service;

import com.nimbusds.jose.proc.BadJOSEException;
import com.nimbusds.jose.proc.BadJWSException;

import com.pxkdxvan.auth.shared.aspect.AspectPriority;
import com.pxkdxvan.auth.shared.dto.ResponseCode;
import com.pxkdxvan.auth.shared.exception.JwtException;
import com.pxkdxvan.auth.shared.logger.aspect.DefaultAspectLogger;
import com.pxkdxvan.auth.shared.logger.LoggerArgs;
import com.pxkdxvan.auth.shared.logger.LoggerBuilder;
import com.pxkdxvan.auth.shared.security.JwtType;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;

import org.springframework.core.annotation.Order;
import org.springframework.security.oauth2.jwt.JwtValidationException;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.Collections;
import java.util.Map;

@Aspect
@Component
@Order(AspectPriority.MEDIUM)
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
final class JwtServiceAspect {

    private static final LoggerBuilder.Prefix SERVICE_PREFIX = LoggerBuilder.Prefix.SERVICE;

    private static final String TYPE_KEY = LoggerArgs.TYPE_KEY;
    private static final String SUBJECT_KEY = LoggerArgs.SUBJECT_KEY;
    private static final String CLAIM_KEY = LoggerArgs.CLAIM_KEY;

    private static final String AFTER_JWT_GENERATE_MESSAGE = "JWT generated";
    private static final String AFTER_JWT_EXTRACT_MESSAGE = "JWT claim extracted";

    private final DefaultAspectLogger defaultAspectLogger;

    private ResponseCode getJwtErrorCode(Throwable thr) {
        return switch (thr) {
            case ParseException ignored -> ResponseCode.TOKEN_MALFORMED;
            case IllegalArgumentException ignored -> ResponseCode.TOKEN_DECODING_INVALID;
            case BadJWSException ignored -> ResponseCode.TOKEN_SIGNATURE_INVALID;
            case BadJOSEException ignored -> ResponseCode.TOKEN_HEADER_INVALID;
            case JwtValidationException ignored -> ResponseCode.TOKEN_VALIDATION_NOT_PASSED;
            default -> ResponseCode.INTERNAL_SERVER_ERROR;
        };
    }

    @Around(value = "execution(* com.pxkdxvan.auth.shared.service.JwtService.generate*(..)) && args(type, subject)", argNames = "joinPoint, type, subject")
    Object logAroundJwtServiceGenerateMethod(ProceedingJoinPoint joinPoint, JwtType type, String subject) {
        try {
            Object result = joinPoint.proceed();

            Map<String, Object> entries = Map.ofEntries(Map.entry(TYPE_KEY, type), Map.entry(SUBJECT_KEY, subject));
            defaultAspectLogger.between(SERVICE_PREFIX, AFTER_JWT_GENERATE_MESSAGE, entries);

            return result;
        } catch (Throwable thr) {
            throw new JwtException(thr.getMessage(), getJwtErrorCode(thr));
        }
    }

    @Around(value = "execution(* com.pxkdxvan.auth.shared.service.JwtService.extract*(..))")
    Object logAroundJwtServiceExtractMethod(ProceedingJoinPoint joinPoint) {
        try {
            Object result = joinPoint.proceed();

            Map<String, Object> entries = Collections.singletonMap(CLAIM_KEY, result);
            defaultAspectLogger.between(SERVICE_PREFIX, AFTER_JWT_EXTRACT_MESSAGE, entries);

            return result;
        } catch (Throwable thr) {
            throw new JwtException(thr.getMessage(), getJwtErrorCode(thr));
        }
    }

}
