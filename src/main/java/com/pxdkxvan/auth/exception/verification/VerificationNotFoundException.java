package com.pxdkxvan.auth.exception.verification;

import com.pxdkxvan.auth.response.ResponseCode;

public final class VerificationNotFoundException extends VerificationException {

    private static final String MSG_PREFIX = "Verification not found";

    public VerificationNotFoundException() {
        super(MSG_PREFIX, ResponseCode.TOKEN_NOT_FOUND);
    }

}
