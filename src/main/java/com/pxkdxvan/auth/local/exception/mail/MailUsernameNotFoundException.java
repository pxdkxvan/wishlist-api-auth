package com.pxkdxvan.auth.local.exception.mail;

public final class MailUsernameNotFoundException extends MailException {

    private static final String MGS_PREFIX = "Mail username not found";

    public MailUsernameNotFoundException() {
        super(MGS_PREFIX);
    }

}
