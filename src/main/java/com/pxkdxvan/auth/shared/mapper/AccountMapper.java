package com.pxkdxvan.auth.shared.mapper;

import com.pxkdxvan.auth.shared.model.Account;
import com.pxkdxvan.auth.shared.model.Authorization;
import com.pxkdxvan.auth.shared.security.AccountDetails;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class AccountMapper {

    public static AccountDetails toAccountDetails(Authorization authorization) {
        Account account = authorization.getAccount();
        return AccountDetails
                .builder()
                .id(account.getId())
                .username(account.getUsername())
                .email(account.getEmail())
                .verified(account.getVerified())
                .password(authorization.getPassword())
                .roles(account.getRoles())
                .build();
    }

    public static Account toAccount(AccountDetails accountDetails) {
        return Account
                .builder()
                .id(accountDetails.getId())
                .username(accountDetails.getUsername())
                .email(accountDetails.getEmail())
                .verified(accountDetails.getVerified())
                .roles(accountDetails.getRoles())
                .build();
    }

}
