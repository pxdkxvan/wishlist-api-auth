package com.pxkdxvan.auth.shared.controller;

import com.pxkdxvan.auth.local.exception.auth.AuthorizationException;
import com.pxkdxvan.auth.local.exception.mail.MailException;
import com.pxkdxvan.auth.local.exception.verification.VerificationException;
import com.pxkdxvan.auth.oauth2.exception.ProviderException;
import com.pxkdxvan.auth.shared.builder.SimpleResponseBuilder;
import com.pxkdxvan.auth.shared.dto.ResponseCode;
import com.pxkdxvan.auth.shared.dto.ResponseDTO;
import com.pxkdxvan.auth.shared.exception.JwtException;
import com.pxkdxvan.auth.shared.exception.PropertyNotFoundException;
import com.pxkdxvan.auth.shared.exception.RoleNotFoundException;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestCookieException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public class ExceptionController {

    private final SimpleResponseBuilder simpleResponseBuilder;

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ResponseDTO<Void>> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        return simpleResponseBuilder.buildResponse(HttpStatus.BAD_REQUEST, ResponseCode.VALIDATION_FAILED);
    }

    @ExceptionHandler(MissingRequestCookieException.class)
    public ResponseEntity<ResponseDTO<Void>> handleMissingRequestCookieException(MissingRequestCookieException e) {
        return simpleResponseBuilder.buildResponse(HttpStatus.UNAUTHORIZED, ResponseCode.MISSING_REFRESH_TOKEN);
    }

    @ExceptionHandler(AuthorizationException.class)
    public ResponseEntity<ResponseDTO<Void>> handleAccountException(AuthorizationException e) {
        ResponseCode errorCode = e.getCode();

        HttpStatus status = switch (errorCode) {
            case EMAIL_ALREADY_TAKEN, USERNAME_ALREADY_TAKEN -> HttpStatus.CONFLICT;
            case ACCOUNT_NOT_FOUND, LOGIN_NOT_FOUND -> HttpStatus.NOT_FOUND;
            case ACCOUNT_NOT_VERIFIED -> HttpStatus.UNAUTHORIZED;
            default -> HttpStatus.BAD_REQUEST;
        };

        return simpleResponseBuilder.buildResponse(status, errorCode);
    }

    @ExceptionHandler(RoleNotFoundException.class)
    public ResponseEntity<ResponseDTO<Void>> handleRoleNotFoundException(RoleNotFoundException e) {
        return simpleResponseBuilder.buildResponse(HttpStatus.NOT_FOUND, ResponseCode.ROLE_NOT_FOUND);
    }

    @ExceptionHandler(InternalAuthenticationServiceException.class)
    public ResponseEntity<ResponseDTO<Void>> handleInternalAuthenticationServiceException(InternalAuthenticationServiceException e) {
        return simpleResponseBuilder.buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, ResponseCode.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(JwtException.class)
    public ResponseEntity<ResponseDTO<Void>> handleJwtException(JwtException e) {
        return simpleResponseBuilder.buildResponse(HttpStatus.UNAUTHORIZED, e.getCode());
    }

    @ExceptionHandler(MailException.class)
    public ResponseEntity<ResponseDTO<Void>> handleMailException(MailException e) {
        return simpleResponseBuilder.buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, ResponseCode.EMAIL_SENDING_ERROR);
    }

    @ExceptionHandler(VerificationException.class)
    public ResponseEntity<ResponseDTO<Void>> handleVerificationException(VerificationException e) {
        return simpleResponseBuilder.buildResponse(HttpStatus.GONE, ResponseCode.TOKEN_VALIDATION_NOT_PASSED);
    }

    @ExceptionHandler(OAuth2AuthenticationException.class)
    public ResponseEntity<ResponseDTO<Void>> handleOAuth2AuthenticationException(OAuth2AuthenticationException e) {
        return simpleResponseBuilder.buildResponse(HttpStatus.UNAUTHORIZED, ResponseCode.OAUTH2_AUTHENTICATION_ERROR);
    }

    @ExceptionHandler(ProviderException.class)
    public ResponseEntity<ResponseDTO<Void>> handleProviderException(ProviderException e) {
        return simpleResponseBuilder.buildResponse(HttpStatus.UNAUTHORIZED, ResponseCode.OAUTH2_AUTHENTICATION_ERROR);
    }

    @ExceptionHandler(PropertyNotFoundException.class)
    public ResponseEntity<ResponseDTO<Void>> handlePropertyNotFoundException(PropertyNotFoundException e) {
        return simpleResponseBuilder.buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, ResponseCode.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ResponseDTO<Void>> handleRuntimeException(RuntimeException e) {
        return simpleResponseBuilder.buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, ResponseCode.INTERNAL_SERVER_ERROR);
    }

}
