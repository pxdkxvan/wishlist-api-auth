package com.pxkdxvan.auth.local.exception.auth;

import com.pxkdxvan.auth.shared.dto.ResponseCode;

public final class UsernameAlreadyTakenException extends AuthorizationException {

    private static final String MSG_PREFIX = "Username is already taken";

    public UsernameAlreadyTakenException(String login) {
        super(MSG_PREFIX, login, ResponseCode.USERNAME_ALREADY_TAKEN);
    }

}
