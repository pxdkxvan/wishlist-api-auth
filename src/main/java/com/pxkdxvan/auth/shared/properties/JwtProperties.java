package com.pxkdxvan.auth.shared.properties;

import com.pxkdxvan.auth.shared.exception.JWTSecretKeyNotFoundException;
import com.pxkdxvan.auth.shared.utils.OptionalHelper;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

import java.time.Duration;

@ConfigurationProperties(prefix = "app.jwt")
public record JwtProperties(String secretKey, Access access, Refresh refresh, Temporary temporary,
                            @NestedConfigurationProperty VerificationProperties verificationProperties) {

    private static final Duration DEFAULT_ACCESS_TOKEN_DURATION = Duration.ofMinutes(30);
    private static final Duration DEFAULT_REFRESH_TOKEN_DURATION = Duration.ofHours(6);

    public JwtProperties {
        secretKey = OptionalHelper.getOrThrow(secretKey, JWTSecretKeyNotFoundException::new);
        access = OptionalHelper.getOrCreate(access, () -> new Access(null));
        refresh = OptionalHelper.getOrCreate(refresh, () -> new Refresh(null));
        temporary = OptionalHelper.getOrCreate(temporary, () -> new Temporary(verificationProperties.lifetime()));
    }

    public record Access(Duration duration) {
        public Access {
            duration = OptionalHelper.getOrDefault(duration, DEFAULT_ACCESS_TOKEN_DURATION);
        }
    }

    public record Refresh(Duration duration) {
        public Refresh {
            duration = OptionalHelper.getOrDefault(duration, DEFAULT_REFRESH_TOKEN_DURATION);
        }
    }

    public record Temporary(Duration duration) {}

}
