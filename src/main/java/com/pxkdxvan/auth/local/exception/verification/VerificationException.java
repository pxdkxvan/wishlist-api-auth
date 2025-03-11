package com.pxkdxvan.auth.local.exception.verification;

public abstract class VerificationException extends RuntimeException {
    VerificationException(String message) {
        super(message);
    }
}
