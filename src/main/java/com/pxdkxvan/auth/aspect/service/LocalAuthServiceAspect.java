package com.pxdkxvan.auth.aspect.service;

import com.pxdkxvan.auth.exception.auth.AccountNotVerified;
import com.pxdkxvan.auth.exception.auth.LoginNotFoundException;
import com.pxdkxvan.auth.exception.auth.PasswordMismatchException;
import com.pxdkxvan.auth.util.EmailMasker;
import com.pxdkxvan.auth.aspect.AspectPriority;
import com.pxdkxvan.auth.logger.aspect.DefaultAspectLogger;
import com.pxdkxvan.auth.logger.LoggerArgs;
import com.pxdkxvan.auth.logger.LoggerBuilder;
import com.pxdkxvan.auth.model.Account;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Map;

@Aspect
@Component
@Order(AspectPriority.MEDIUM)
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
final class LocalAuthServiceAspect {

    private static final LoggerBuilder.Prefix PREFIX = LoggerBuilder.Prefix.SERVICE;

    private static final String ACCOUNT_KEY = LoggerArgs.ACCOUNT_KEY;

    private static final String AFTER_LOCAL_LOGIN_MESSAGE = "Logged in locally";
    private static final String AFTER_LOCAL_REGISTER_MESSAGE = "Registered locally";

    private final DefaultAspectLogger defaultAspectLogger;

    @Around(value = "execution(* com.pxdkxvan.auth.service.LocalAuthService.login(..)) && args(login, ..)", argNames = "joinPoint, login")
    Object logAroundLocalAuthServiceLoginMethod(ProceedingJoinPoint joinPoint, String login) {
        try {
            Account account = (Account) joinPoint.proceed();

            Map<String, Object> entries = Collections.singletonMap(ACCOUNT_KEY, account);
            defaultAspectLogger.between(PREFIX, AFTER_LOCAL_LOGIN_MESSAGE, entries);

            return joinPoint.proceed();
        } catch (Throwable thr) {
            throw switch (thr) {
                case DisabledException ignored -> new AccountNotVerified(EmailMasker.mask(login));
                case InternalAuthenticationServiceException e ->
                        (e.getCause() instanceof LoginNotFoundException) ?
                                (LoginNotFoundException) e.getCause() : e;
                case BadCredentialsException ignored -> new PasswordMismatchException(EmailMasker.mask(login));
                default -> new RuntimeException(thr);
            };
        }
    }

    @AfterReturning(value = "execution(* com.pxdkxvan.auth.service.LocalAuthService.register(..))", returning = "account")
    void logAfterReturningAuthServiceRegisterMethod(Account account) {
        Map<String, Object> entries = Collections.singletonMap(ACCOUNT_KEY, account);
        defaultAspectLogger.between(PREFIX, AFTER_LOCAL_REGISTER_MESSAGE, entries);
    }

}
