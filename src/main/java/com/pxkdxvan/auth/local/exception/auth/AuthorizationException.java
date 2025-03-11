package com.pxkdxvan.auth.local.exception.auth;

import com.pxkdxvan.auth.shared.dto.ResponseCode;

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
