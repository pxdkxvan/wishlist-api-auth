package com.pxdkxvan.auth.generator;

import com.pxdkxvan.auth.model.VerificationMethod;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.util.Base64;

@Component
@NoArgsConstructor(access = AccessLevel.PACKAGE)
public final class VerificationGenerator {

    private static final SecureRandom SECURE_RANDOM = new SecureRandom();

    private String token() {
        byte[] bytes = new byte[32];
        SECURE_RANDOM.nextBytes(bytes);
        return Base64
                .getUrlEncoder()
                .withoutPadding()
                .encodeToString(bytes);
    }

    private String code() {
        int code = 100_000 + SECURE_RANDOM.nextInt(900_000);
        return Integer.toString(code);
    }

    public String random(VerificationMethod method) {
        return switch (method) {
            case LINK -> token();
            case CODE -> code();
        };
    }

}
