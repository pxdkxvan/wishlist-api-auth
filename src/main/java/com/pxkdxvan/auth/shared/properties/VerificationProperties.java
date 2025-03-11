package com.pxkdxvan.auth.shared.properties;

import com.pxkdxvan.auth.shared.utils.OptionalHelper;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;

@ConfigurationProperties(prefix = "app.verification")
public record VerificationProperties(Duration lifetime, Key key) {

    private static final Duration DEFAULT_LIFETIME = Duration.ofHours(12);

    private static final Duration DEFAULT_CODE_DURATION = Duration.ofMinutes(10);

    public VerificationProperties {
        lifetime = OptionalHelper.getOrDefault(lifetime, DEFAULT_LIFETIME);
        key = OptionalHelper.getOrCreate(key, () -> new Key(null));
    }

    public record Key(Code code) {

        public Key {
            code = OptionalHelper.getOrCreate(code, () -> new Code(null));
        }

        public record Code(Duration duration) {
            public Code {
                duration = OptionalHelper.getOrDefault(duration, DEFAULT_CODE_DURATION);
            }
        }

    }

}
