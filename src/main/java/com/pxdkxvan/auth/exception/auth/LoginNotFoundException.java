package com.pxdkxvan.auth.exception.auth;

import com.pxdkxvan.auth.response.ResponseCode;

public final class LoginNotFoundException extends AuthorizationException {

    private static final String MSG_PREFIX = "Username or email not found";

    public LoginNotFoundException(String login) {
        super(MSG_PREFIX, login, ResponseCode.LOGIN_NOT_FOUND);
    }

}
