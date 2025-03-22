package com.pxdkxvan.auth.exception.auth;

import com.pxdkxvan.auth.response.ResponseCode;

public final class UsernameAlreadyTakenException extends AuthorizationException {

    private static final String MSG_PREFIX = "Username is already taken";

    public UsernameAlreadyTakenException(String login) {
        super(MSG_PREFIX, login, ResponseCode.USERNAME_ALREADY_TAKEN);
    }

}
