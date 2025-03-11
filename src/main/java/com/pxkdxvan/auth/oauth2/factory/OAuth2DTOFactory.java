package com.pxkdxvan.auth.oauth2.factory;

import com.pxkdxvan.auth.oauth2.dto.OAuth2AccountData;

import org.springframework.stereotype.Component;

@Component
public final class OAuth2DTOFactory {
    public OAuth2AccountData create(String username, String email, String providerId) {
        return OAuth2AccountData
                .builder()
                .username(username)
                .email(email)
                .providerId(providerId)
                .build();
    }
}
