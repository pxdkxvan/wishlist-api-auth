package com.pxdkxvan.auth.config;

import com.nimbusds.jose.jwk.source.ImmutableSecret;

import com.pxdkxvan.auth.properties.JwtProperties;
import com.pxdkxvan.auth.util.KeyDecoder;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;

@Configuration
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
class JwtConfig {

    @Bean
    JwtEncoder jwtEncoder(JwtProperties jwtProperties) {
        return new NimbusJwtEncoder(new ImmutableSecret<>(KeyDecoder.decode(jwtProperties.secretKey())));
    }

    @Bean
    JwtDecoder jwtDecoder(JwtProperties jwtProperties) {
        return NimbusJwtDecoder
                .withSecretKey(KeyDecoder.decode(jwtProperties.secretKey()))
                .build();
    }

}
