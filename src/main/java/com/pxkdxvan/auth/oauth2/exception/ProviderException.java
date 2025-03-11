package com.pxkdxvan.auth.oauth2.exception;

import com.pxkdxvan.auth.shared.model.Provider;

import lombok.Getter;

@Getter
public abstract class ProviderException extends RuntimeException {

    private final Provider provider;

    public ProviderException(String message, Provider provider) {
        super(message);
        this.provider = provider;
    }

}
