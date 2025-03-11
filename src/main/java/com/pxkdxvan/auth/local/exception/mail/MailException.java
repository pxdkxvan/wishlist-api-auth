package com.pxkdxvan.auth.local.exception.mail;

public abstract class MailException extends RuntimeException {

    MailException(String message) {
        super(message);
    }

    MailException(String message, Throwable cause) {
        super(message, cause);
    }

}
