package com.pxkdxvan.auth.local.exception.auth;

import com.pxkdxvan.auth.shared.dto.ResponseCode;

import lombok.Getter;

@Getter
public final class AccountNotFoundException extends AuthorizationException {

    private static final String MSG_PREFIX = "Account not found";

    public AccountNotFoundException(String accountId) {
        super(MSG_PREFIX, accountId, ResponseCode.ACCOUNT_NOT_FOUND);
    }

}
