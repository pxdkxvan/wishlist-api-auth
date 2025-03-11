package com.pxkdxvan.auth.local.exception.verification;

public final class VerificationKeyExpiredException extends VerificationException {

    private static final String MSG_PREFIX = "Verification key expired";

    public VerificationKeyExpiredException() {
        super(MSG_PREFIX);
    }

}
