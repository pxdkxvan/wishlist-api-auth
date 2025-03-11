package com.pxkdxvan.auth.oauth2.provider;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum OAuth2ProviderAttrs {

    GOOGLE(null, "email", "sub"),
    YANDEX("login", "default_email", "id"),
    GITHUB("login", "email", "id"),
    GITLAB("username", "email", "id"),
    DISCORD("username", "email", "id");

    private final String usernameAttr;
    private final String emailAttr;
    private final String providerIdAttr;

}
