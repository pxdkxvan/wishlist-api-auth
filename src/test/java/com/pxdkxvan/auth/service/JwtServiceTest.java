package com.pxdkxvan.auth.service;

import com.pxdkxvan.auth.properties.JwtProperties;
import com.pxdkxvan.auth.security.JwtType;
import com.pxdkxvan.auth.support.factory.TestJwtFactory;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.ConfigDataApplicationContextInitializer;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import static com.pxdkxvan.auth.support.config.TestConstants.*;

import static org.assertj.core.api.Assertions.assertThat;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@EnableConfigurationProperties(JwtProperties.class)
@RequiredArgsConstructor(onConstructor_ = @Autowired, access = AccessLevel.PACKAGE)
@SpringJUnitConfig(classes = {JwtService.class, TestJwtFactory.class}, initializers = ConfigDataApplicationContextInitializer.class)
class JwtServiceTest {

    @MockitoBean
    private final JwtEncoder jwtEncoder;
    @MockitoBean
    private final JwtDecoder jwtDecoder;

    private final JwtService jwtService;

    private final TestJwtFactory testJwtFactory;

    @Test
    void generateTokenSuccess() {
        Jwt testJwt = testJwtFactory.createJwt();
        when(jwtEncoder.encode(any(JwtEncoderParameters.class))).thenReturn(testJwt);
        assertThat(jwtService.generateToken(JwtType.ACCESS, JWT_SUBJECT)).isEqualTo(JWT_VALUE);
        verify(jwtEncoder).encode(any(JwtEncoderParameters.class));
    }

    @Test
    void extractSubjectSuccess() {
        Jwt testJwt = testJwtFactory.createJwt();
        when(jwtDecoder.decode(JWT_VALUE)).thenReturn(testJwt);
        assertThat(jwtService.extractSubject(JWT_VALUE)).isEqualTo(JWT_SUBJECT);
        verify(jwtDecoder).decode(JWT_VALUE);
    }

    @Test
    void extractExpiresAtSuccess() {
        Jwt testJwt = testJwtFactory.createJwt();
        when(jwtDecoder.decode(JWT_VALUE)).thenReturn(testJwt);
        assertThat(jwtService.extractExpiresAt(JWT_VALUE)).isEqualTo(JWT_EXPIRES_AT);
        verify(jwtDecoder).decode(JWT_VALUE);
    }

    @Test
    void extractClaimSuccess() {
        Jwt testJwt = testJwtFactory.createJwt();
        when(jwtDecoder.decode(JWT_VALUE)).thenReturn(testJwt);
        assertThat(jwtService.extractClaim(JWT_SUBJECT_CLAIM, JWT_VALUE)).isEqualTo(JWT_SUBJECT);
        verify(jwtDecoder).decode(JWT_VALUE);
    }

}
