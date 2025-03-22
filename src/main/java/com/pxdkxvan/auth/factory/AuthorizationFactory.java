package com.pxdkxvan.auth.factory;

import com.pxdkxvan.auth.model.Account;
import com.pxdkxvan.auth.model.Authorization;
import com.pxdkxvan.auth.model.Provider;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public final class AuthorizationFactory {

    private final PasswordEncoder passwordEncoder;

    public Authorization createOAuth2(Account account, Provider provider, String providerId) {
        return Authorization
                .builder()
                .account(account)
                .provider(provider)
                .providerId(providerId)
                .build();
    }

    public Authorization createLocal(Account account, String password) {
        return Authorization
                .builder()
                .account(account)
                .provider(Provider.LOCAL)
                .providerId(String.valueOf(account.getId()))
                .password(passwordEncoder.encode(password))
                .build();
    }

}
