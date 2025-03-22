package com.pxdkxvan.auth.exception.mail;

public final class MailPasswordNotFoundException extends MailException {

    private static final String MGS_PREFIX = "Mail password not found";

    public MailPasswordNotFoundException() {
        super(MGS_PREFIX);
    }

}
