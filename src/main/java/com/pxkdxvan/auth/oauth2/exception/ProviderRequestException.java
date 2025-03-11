package com.pxkdxvan.auth.oauth2.exception;

import com.pxkdxvan.auth.shared.model.Provider;

public final class ProviderRequestException extends ProviderException {
    public ProviderRequestException(String message, Provider provider) {
        super(message, provider);
    }
}
