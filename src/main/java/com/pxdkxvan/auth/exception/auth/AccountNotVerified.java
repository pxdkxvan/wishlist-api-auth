package com.pxdkxvan.auth.exception.auth;

import com.pxdkxvan.auth.response.ResponseCode;

public final class AccountNotVerified extends AuthorizationException {

    private static final String MSG_PREFIX = "Account not verified";

    public AccountNotVerified(String login) {
        super(MSG_PREFIX, login, ResponseCode.ACCOUNT_NOT_VERIFIED);
    }

}
