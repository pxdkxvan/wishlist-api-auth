package com.pxdkxvan.auth.exception.auth;

import com.pxdkxvan.auth.response.ResponseCode;

public final class EmailAlreadyTakenException extends AuthorizationException {

    private static final String MSG_PREFIX = "Email is already taken";

    public EmailAlreadyTakenException(String login) {
        super(MSG_PREFIX, login, ResponseCode.EMAIL_ALREADY_TAKEN);
    }

}
