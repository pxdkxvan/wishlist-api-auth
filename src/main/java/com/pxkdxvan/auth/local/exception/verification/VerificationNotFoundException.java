package com.pxkdxvan.auth.local.exception.verification;

public final class VerificationNotFoundException extends VerificationException {

    private static final String MSG_PREFIX = "Verification not found";

    public VerificationNotFoundException() {
        super(MSG_PREFIX);
    }

}
