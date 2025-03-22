package com.pxdkxvan.auth.exception.verification;

import com.pxdkxvan.auth.response.ResponseCode;

public final class VerificationKeyExpiredException extends VerificationException {

    private static final String MSG_PREFIX = "Verification key expired";

    public VerificationKeyExpiredException() {
        super(MSG_PREFIX, ResponseCode.TOKEN_VALIDATION_NOT_PASSED);
    }

}
