package com.pxdkxvan.auth.exception.provider;

import com.pxdkxvan.auth.model.Provider;

import lombok.Getter;

@Getter
public abstract class ProviderException extends RuntimeException {

    private final Provider provider;

    public ProviderException(String message, Provider provider) {
        super(message);
        this.provider = provider;
    }

}
