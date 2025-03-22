package com.pxdkxvan.auth.exception.verification;

import com.pxdkxvan.auth.response.ResponseCode;
import lombok.Getter;

@Getter
public abstract class VerificationException extends RuntimeException {

    private final ResponseCode code;

    VerificationException(String message, ResponseCode code) {
        super(message);
        this.code = code;
    }

}
