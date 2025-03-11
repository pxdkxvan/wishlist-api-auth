package com.pxkdxvan.auth.shared.exception;

public final class JWTSecretKeyNotFoundException extends RuntimeException {

    private static final String MESSAGE = "JWT Secret Key not found";

    public JWTSecretKeyNotFoundException() {
        super(MESSAGE);
    }

}
