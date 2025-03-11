package com.pxkdxvan.auth.oauth2.exception;

import com.pxkdxvan.auth.shared.model.Provider;

import lombok.Getter;

@Getter
public final class ProviderNotFoundException extends ProviderException {

    private static final String MSG_PREFIX = "Provider not found";

    public ProviderNotFoundException(Provider provider) {
        super(MSG_PREFIX, provider);
    }

}
