package com.pxdkxvan.auth.aspect.core;

import com.pxdkxvan.auth.exception.auth.AuthorizationException;
import com.pxdkxvan.auth.exception.mail.MailException;
import com.pxdkxvan.auth.exception.verification.VerificationException;
import com.pxdkxvan.auth.exception.provider.ProviderException;
import com.pxdkxvan.auth.aspect.AspectPriority;
import com.pxdkxvan.auth.exception.jwt.JwtException;
import com.pxdkxvan.auth.exception.PropertyNotFoundException;
import com.pxdkxvan.auth.exception.auth.RoleNotFoundException;
import com.pxdkxvan.auth.logger.LoggerArgs;
import com.pxdkxvan.auth.logger.LoggerBuilder;
import com.pxdkxvan.auth.logger.LoggerErrorArgs;
import com.pxdkxvan.auth.logger.aspect.ErrorAspectLogger;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;

import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestCookieException;

import java.util.Collections;
import java.util.Map;

@Aspect
@Component
@Order(AspectPriority.HIGHEST)
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
final class ExceptionAspect {

    private static final LoggerBuilder.Prefix WARNING_PREFIX = LoggerBuilder.Prefix.WARNING;
    private static final LoggerBuilder.Prefix ERROR_PREFIX = LoggerBuilder.Prefix.ERROR;

    private static final String ACCOUNT_KEY = LoggerArgs.ACCOUNT_KEY;
    private static final String PROVIDER_KEY = LoggerArgs.PROVIDER_KEY;
    private static final String PROPERTY_KEY = LoggerArgs.PROPERTY_KEY;

    private static final String REQUEST_VALIDATION_ERROR = LoggerErrorArgs.REQUEST_VALIDATION_ERROR;
    private static final String COOKIE_VALIDATION_ERROR = LoggerErrorArgs.COOKIE_VALIDATION_ERROR;
    private static final String AUTH_ERROR = LoggerErrorArgs.AUTH_ERROR;
    private static final String ROLE_ERROR = LoggerErrorArgs.ROLE_ERROR;
    private static final String INTERNAL_AUTHENTICATION_SERVICE_ERROR = LoggerErrorArgs.INTERNAL_AUTHENTICATION_SERVICE_ERROR;
    private static final String JWT_ERROR = LoggerErrorArgs.JWT_ERROR;
    private static final String MAIL_ERROR = LoggerErrorArgs.MAIL_ERROR;
    private static final String VERIFICATION_ERROR = LoggerErrorArgs.VERIFICATION_ERROR;
    private static final String OAUTH2_AUTHENTICATION_ERROR = LoggerErrorArgs.OAUTH2_AUTHENTICATION_ERROR;
    private static final String PROVIDER_ERROR = LoggerErrorArgs.PROVIDER_ERROR;
    private static final String PROPERTY_ERROR = LoggerErrorArgs.PROPERTY_ERROR;
    private static final String INTERNAL_SERVER_ERROR = LoggerErrorArgs.INTERNAL_SERVER_ERROR;

    private final ErrorAspectLogger errorAspectLogger;

    private void logAuthException(AuthorizationException e) {
        Map<String, Object> entries = Map.ofEntries(Map.entry(AUTH_ERROR, e.getMessage()), Map.entry(ACCOUNT_KEY, e.getIdentifier()));
        errorAspectLogger.after(WARNING_PREFIX, entries);
    }

    private void logProviderException(ProviderException e) {
        Map<String, Object> entries = Map.ofEntries(Map.entry(PROVIDER_ERROR, e.getMessage()), Map.entry(PROVIDER_KEY, e.getProvider()));
        errorAspectLogger.after(WARNING_PREFIX, entries);
    }

    private void logPropertyException(PropertyNotFoundException e) {
        Map<String, Object> entries = Map.ofEntries(Map.entry(PROPERTY_ERROR, e.getMessage()), Map.entry(PROPERTY_KEY, e.getProperty()));
        errorAspectLogger.after(ERROR_PREFIX, entries, e);
    }

    private void logWarningException(String errorAttr, Throwable thr) {
        Map<String, Object> entries = Collections.singletonMap(errorAttr, thr.getMessage());
        errorAspectLogger.after(WARNING_PREFIX, entries);
    }

    private void logErrorException(String errorAttr, Throwable thr) {
        Map<String, Object> entries = Collections.singletonMap(errorAttr, thr.getMessage());
        errorAspectLogger.after(ERROR_PREFIX, entries, thr);
    }

    @Before(value = "@within(org.springframework.web.bind.annotation.RestControllerAdvice) && args(thr)", argNames = "thr")
    void logAfterThrowingExceptionMethods(Throwable thr) {
        switch (thr) {
            case MethodArgumentNotValidException e -> logWarningException(REQUEST_VALIDATION_ERROR, e);
            case MissingRequestCookieException e -> logWarningException(COOKIE_VALIDATION_ERROR, e);
            case AuthorizationException e -> logAuthException(e);
            case RoleNotFoundException e -> logErrorException(ROLE_ERROR, e);
            case InternalAuthenticationServiceException e -> logErrorException(INTERNAL_AUTHENTICATION_SERVICE_ERROR, e);
            case JwtException e -> logWarningException(JWT_ERROR, e);
            case MailException e -> logErrorException(MAIL_ERROR, e);
            case VerificationException e -> logWarningException(VERIFICATION_ERROR, e);
            case OAuth2AuthenticationException e -> logWarningException(OAUTH2_AUTHENTICATION_ERROR, e);
            case ProviderException e -> logProviderException(e);
            case PropertyNotFoundException e -> logPropertyException(e);
            default -> logErrorException(INTERNAL_SERVER_ERROR, thr);
        }
    }

}
