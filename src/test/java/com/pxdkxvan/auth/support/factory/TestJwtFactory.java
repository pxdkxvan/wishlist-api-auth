package com.pxdkxvan.auth.support.factory;

import com.pxdkxvan.auth.factory.OAuth2DataFactory;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import org.springframework.boot.test.context.TestComponent;
import org.springframework.context.annotation.Import;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.Jwt;

import static com.pxdkxvan.auth.support.config.TestConstants.*;

@TestComponent
@Import({OAuth2DataFactory.class})
@NoArgsConstructor(access = AccessLevel.PACKAGE)
public final class TestJwtFactory {
    public Jwt createJwt() {
        return Jwt
                .withTokenValue(JWT_VALUE)
                .header(JWT_ALGORITHM_CLAIM, MacAlgorithm.HS256)
                .header(JWT_TYPE_CLAIM, JWT_TYPE)
                .issuer(JWT_ISSUER)
                .subject(JWT_SUBJECT)
                .issuedAt(JWT_ISSUED_AT)
                .expiresAt(JWT_EXPIRES_AT)
                .build();
    }
}
