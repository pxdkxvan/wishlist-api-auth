package com.pxkdxvan.auth.shared.factory;

import com.pxkdxvan.auth.shared.model.Account;
import com.pxkdxvan.auth.shared.model.Authorization;
import com.pxkdxvan.auth.shared.model.Provider;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
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
