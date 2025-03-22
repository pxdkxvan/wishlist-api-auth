package com.pxdkxvan.auth.factory;

import com.pxdkxvan.auth.model.Account;
import com.pxdkxvan.auth.model.Verification;
import com.pxdkxvan.auth.model.VerificationMethod;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;

@Component
@NoArgsConstructor(access = AccessLevel.PACKAGE)
public final class VerificationFactory {
    public Verification create(String key, VerificationMethod method, Account account, ZonedDateTime expiration) {
        return Verification
                .builder()
                .key(key)
                .method(method)
                .account(account)
                .expiresAt(expiration)
                .build();
    }
}
