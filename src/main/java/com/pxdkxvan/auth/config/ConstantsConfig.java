package com.pxdkxvan.auth.config;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ConstantsConfig {
    public static final String APP_NAME = "Task Manager App";
    public static final String CORRELATION_HEADER = "X-Correlation-Id";
    public static final String REFRESH_COOKIE_NAME = "refresh";
    public static final String VERIFICATION_LINK_PARAM_NAME = "token";
    public static final String ACCOUNT_ID_ATTR_NAME = "accountId";
    public static final String DEFAULT_ENCODING = "UTF-8";
    public static final String DEFAULT_ROLE_NAME = "MEMBER";
    public static final int DEFAULT_MASK_SHOWN_SYMBOLS_AMOUNT = 2;
}
