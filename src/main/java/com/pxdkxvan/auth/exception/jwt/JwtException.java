package com.pxdkxvan.auth.exception.jwt;

import com.pxdkxvan.auth.response.ResponseCode;

import lombok.Getter;

@Getter
public final class JwtException extends RuntimeException {

    private final ResponseCode code;

    public JwtException(String message, ResponseCode code) {
        super(message);
        this.code = code;
    }

}
