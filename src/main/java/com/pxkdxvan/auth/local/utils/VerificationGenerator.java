package com.pxkdxvan.auth.local.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.security.SecureRandom;
import java.util.Base64;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class VerificationGenerator {

    private static final SecureRandom SECURE_RANDOM = new SecureRandom();

    public static String token() {
        byte[] bytes = new byte[32];
        SECURE_RANDOM.nextBytes(bytes);
        return Base64
                .getUrlEncoder()
                .withoutPadding()
                .encodeToString(bytes);
    }

    public static String code() {
        int code = 100_000 + SECURE_RANDOM.nextInt(900_000);
        return Integer.toString(code);
    }

}
