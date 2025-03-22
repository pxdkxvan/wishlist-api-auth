package com.pxdkxvan.auth.factory;

import com.pxdkxvan.auth.model.OAuth2AccountData;

import org.springframework.stereotype.Component;

@Component
public final class OAuth2DataFactory {
    public OAuth2AccountData create(String username, String email, String providerId) {
        return OAuth2AccountData
                .builder()
                .username(username)
                .email(email)
                .providerId(providerId)
                .build();
    }
}
