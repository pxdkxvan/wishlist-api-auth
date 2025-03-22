package com.pxdkxvan.auth.exception.mail;

public abstract class MailException extends RuntimeException {

    MailException(String message) {
        super(message);
    }

    MailException(String message, Throwable cause) {
        super(message, cause);
    }

}
