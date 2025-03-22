package com.pxdkxvan.auth.exception.provider;

import com.pxdkxvan.auth.model.Provider;

public final class InsufficientOAuth2UserDataException extends ProviderException {

    private static final String MSG_PREFIX = "Insufficient OAuth2 User Data";

    public InsufficientOAuth2UserDataException(Provider provider) {
        super(MSG_PREFIX, provider);
    }
}
