package com.pxkdxvan.auth.local.exception.auth;

import com.pxkdxvan.auth.shared.dto.ResponseCode;

public final class PasswordMismatchException extends AuthorizationException {

    private static final String MSG_PREFIX = "Password mismatch";

    public PasswordMismatchException(String login) {
        super(MSG_PREFIX, login, ResponseCode.PASSWORD_MISMATCH);
    }

}
