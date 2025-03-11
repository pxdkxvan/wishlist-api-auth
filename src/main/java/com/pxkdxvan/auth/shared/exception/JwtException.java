package com.pxkdxvan.auth.shared.exception;

import com.pxkdxvan.auth.shared.dto.ResponseCode;

import lombok.Getter;

@Getter
public final class JwtException extends RuntimeException {

    private final ResponseCode code;

    public JwtException(String message, ResponseCode code) {
        super(message);
        this.code = code;
    }

}
