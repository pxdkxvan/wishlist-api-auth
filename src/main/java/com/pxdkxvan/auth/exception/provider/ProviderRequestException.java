package com.pxdkxvan.auth.exception.provider;

import com.pxdkxvan.auth.model.Provider;

public final class ProviderRequestException extends ProviderException {
    public ProviderRequestException(String message, Provider provider) {
        super(message, provider);
    }
}
