package com.pxdkxvan.auth.support.config;

import com.pxdkxvan.auth.response.ResponseCode;
import com.pxdkxvan.auth.util.TimeUtils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import org.springframework.http.HttpStatus;

import java.time.Instant;
import java.time.ZonedDateTime;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class TestConstants {
    public static final HttpStatus RESPONSE_STATUS = HttpStatus.OK;
    public static final ResponseCode RESPONSE_CODE = ResponseCode.INTERNAL_SERVER_ERROR;
    public static final String COOKIE_NAME = "test";

    public static final String ROLE_NAME = "TEST";

    public static final String ACCOUNT_USERNAME = "test";
    public static final String ACCOUNT_EMAIL = "testing@test.test";
    public static final String ACCOUNT_MASKED_EMAIL = "te*****@****.****";
    public static final String ACCOUNT_PASSWORD = "test123456";

    public static final String AUTHORIZATION_PROVIDER_ID = "test.123";
    public static final String OAUTH2_ACCESS_TOKEN = "ACC3$$^TXK3N";

    public static final String VERIFICATION_TOKEN = "T3$T^TXK3N";
    public static final String VERIFICATION_CODE = "123456";
    public static final ZonedDateTime VERIFICATION_EXPIRES_AT = TimeUtils.MIN;

    public static final String JWT_ALGORITHM_CLAIM = "alg";
    public static final String JWT_TYPE_CLAIM = "typ";
    public static final String JWT_SUBJECT_CLAIM = "sub";

    public static final String JWT_VALUE = "JWTest";
    public static final String JWT_TYPE = "JWT";
    public static final String JWT_ISSUER = "testing-layer";
    public static final String JWT_SUBJECT = "test";
    public static final Instant JWT_ISSUED_AT = Instant.MIN;
    public static final Instant JWT_EXPIRES_AT = Instant.EPOCH;
}
