package com.pxkdxvan.auth.shared.config;

import com.nimbusds.jose.jwk.source.ImmutableSecret;
import com.pxkdxvan.auth.shared.properties.JwtProperties;
import com.pxkdxvan.auth.shared.utils.KeyDecoder;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;

@Configuration
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
