package com.pxdkxvan.auth.controller;

import com.pxdkxvan.auth.exception.auth.AuthorizationException;
import com.pxdkxvan.auth.exception.mail.MailException;
import com.pxdkxvan.auth.exception.verification.VerificationException;
import com.pxdkxvan.auth.exception.provider.ProviderException;
import com.pxdkxvan.auth.response.StatusResponseShaper;
import com.pxdkxvan.auth.response.ResponseCode;
import com.pxdkxvan.auth.dto.ResponseDTO;
import com.pxdkxvan.auth.exception.jwt.JwtException;
import com.pxdkxvan.auth.exception.PropertyNotFoundException;
import com.pxdkxvan.auth.exception.auth.RoleNotFoundException;

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

    private final StatusResponseShaper statusResponseShaper;

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ResponseDTO<Void>> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        return statusResponseShaper.buildResponse(HttpStatus.BAD_REQUEST, ResponseCode.VALIDATION_FAILED);
    }

    @ExceptionHandler(MissingRequestCookieException.class)
    public ResponseEntity<ResponseDTO<Void>> handleMissingRequestCookieException(MissingRequestCookieException e) {
        return statusResponseShaper.buildResponse(HttpStatus.UNAUTHORIZED, ResponseCode.MISSING_REFRESH_TOKEN);
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

        if (errorCode == ResponseCode.PASSWORD_MISMATCH || errorCode == ResponseCode.LOGIN_NOT_FOUND)
            errorCode = ResponseCode.LOGIN_FAILED;

        return statusResponseShaper.buildResponse(status, errorCode);
    }

    @ExceptionHandler(RoleNotFoundException.class)
    public ResponseEntity<ResponseDTO<Void>> handleRoleNotFoundException(RoleNotFoundException e) {
        return statusResponseShaper.buildResponse(HttpStatus.NOT_FOUND, ResponseCode.ROLE_NOT_FOUND);
    }

    @ExceptionHandler(InternalAuthenticationServiceException.class)
    public ResponseEntity<ResponseDTO<Void>> handleInternalAuthenticationServiceException(InternalAuthenticationServiceException e) {
        return statusResponseShaper.buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, ResponseCode.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(JwtException.class)
    public ResponseEntity<ResponseDTO<Void>> handleJwtException(JwtException e) {
        return statusResponseShaper.buildResponse(HttpStatus.UNAUTHORIZED, e.getCode());
    }

    @ExceptionHandler(MailException.class)
    public ResponseEntity<ResponseDTO<Void>> handleMailException(MailException e) {
        return statusResponseShaper.buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, ResponseCode.EMAIL_SENDING_ERROR);
    }

    @ExceptionHandler(VerificationException.class)
    public ResponseEntity<ResponseDTO<Void>> handleVerificationException(VerificationException e) {
        ResponseCode errorCode = e.getCode();

        HttpStatus status = switch (errorCode) {
            case TOKEN_VALIDATION_NOT_PASSED -> HttpStatus.BAD_REQUEST;
            case TOKEN_NOT_FOUND -> HttpStatus.NOT_FOUND;
            default -> HttpStatus.INTERNAL_SERVER_ERROR;
        };

        return statusResponseShaper.buildResponse(status, ResponseCode.VERIFICATION_FAILED);
    }

    @ExceptionHandler(OAuth2AuthenticationException.class)
    public ResponseEntity<ResponseDTO<Void>> handleOAuth2AuthenticationException(OAuth2AuthenticationException e) {
        return statusResponseShaper.buildResponse(HttpStatus.UNAUTHORIZED, ResponseCode.OAUTH2_AUTHENTICATION_ERROR);
    }

    @ExceptionHandler(ProviderException.class)
    public ResponseEntity<ResponseDTO<Void>> handleProviderException(ProviderException e) {
        return statusResponseShaper.buildResponse(HttpStatus.UNAUTHORIZED, ResponseCode.OAUTH2_AUTHENTICATION_ERROR);
    }

    @ExceptionHandler(PropertyNotFoundException.class)
    public ResponseEntity<ResponseDTO<Void>> handlePropertyNotFoundException(PropertyNotFoundException e) {
        return statusResponseShaper.buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, ResponseCode.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ResponseDTO<Void>> handleRuntimeException(RuntimeException e) {
        return statusResponseShaper.buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, ResponseCode.INTERNAL_SERVER_ERROR);
    }

}
