package com.pxkdxvan.auth.oauth2.exception;

import com.pxkdxvan.auth.shared.model.Provider;

public final class MatchingEmailNotFoundException extends ProviderException {
    public MatchingEmailNotFoundException(String message, Provider provider) {
        super(message, provider);
    }
}
