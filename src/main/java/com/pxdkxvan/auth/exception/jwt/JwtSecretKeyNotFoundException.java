package com.pxdkxvan.auth.exception.jwt;

public final class JwtSecretKeyNotFoundException extends RuntimeException {

    private static final String MESSAGE = "JWT Secret Key not found";

    public JwtSecretKeyNotFoundException() {
        super(MESSAGE);
    }

}
