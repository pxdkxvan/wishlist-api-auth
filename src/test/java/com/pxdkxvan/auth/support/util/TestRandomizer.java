package com.pxdkxvan.auth.support.util;

import com.pxdkxvan.auth.model.Provider;
import com.pxdkxvan.auth.model.VerificationMethod;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import org.springframework.boot.test.context.TestComponent;

import java.util.EnumSet;
import java.util.concurrent.ThreadLocalRandom;

import static com.pxdkxvan.auth.support.config.TestConstants.ACCOUNT_EMAIL;
import static com.pxdkxvan.auth.support.config.TestConstants.ACCOUNT_USERNAME;

@TestComponent
@NoArgsConstructor(access = AccessLevel.PACKAGE)
public final class TestRandomizer {

    public <T> T random(T[] array) {
        int index = ThreadLocalRandom.current().nextInt(array.length);
        return array[index];
    }

    public Provider randomProvider() {
        return random(Provider.values());
    }

    public Provider randomOAuth2Provider() {
        return random(EnumSet
                .allOf(Provider.class)
                .stream()
                .filter(provider -> provider != Provider.LOCAL)
                .toArray(Provider[]::new));
    }

    public VerificationMethod randomVerificationMethod() {
        return random(VerificationMethod.values());
    }

    public String randomLogin() {
        return random(new String[]{ACCOUNT_USERNAME, ACCOUNT_EMAIL});
    }

}
