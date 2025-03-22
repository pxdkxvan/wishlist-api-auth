package com.pxdkxvan.auth.exception.auth;

import com.pxdkxvan.auth.response.ResponseCode;

import lombok.Getter;

@Getter
public abstract class AuthorizationException extends RuntimeException {

    private final String identifier;
    private final ResponseCode code;

    AuthorizationException(String message, String identifier, ResponseCode code) {
        super(message);
        this.identifier = identifier;
        this.code = code;
    }

}
