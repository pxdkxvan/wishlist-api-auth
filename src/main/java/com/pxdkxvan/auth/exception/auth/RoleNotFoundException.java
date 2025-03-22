package com.pxdkxvan.auth.exception.auth;

public final class RoleNotFoundException extends RuntimeException {

    private static final String MSG_PREFIX = "Unknown role";

    public RoleNotFoundException(String role) {
        super("%s -> %s".formatted(MSG_PREFIX, role));
    }

}
