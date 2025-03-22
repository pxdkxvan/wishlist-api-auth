package com.pxdkxvan.auth.exception.provider;

import com.pxdkxvan.auth.model.Provider;

public final class MatchingEmailNotFoundException extends ProviderException {
    public MatchingEmailNotFoundException(String message, Provider provider) {
        super(message, provider);
    }
}
