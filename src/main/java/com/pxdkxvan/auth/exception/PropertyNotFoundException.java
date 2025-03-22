package com.pxdkxvan.auth.exception;

import lombok.Getter;

@Getter
public final class PropertyNotFoundException extends RuntimeException {

    private final String property;

    public PropertyNotFoundException(String message, String property) {
        super(message);
        this.property = property;
    }

}
