package com.pxkdxvan.auth.shared.config;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ConstantsConfig {
    public static final String APP_NAME = "Task Manager App";
    public static final String CORRELATION_HEADER = "X-Correlation-Id";
    public static final String REFRESH_COOKIE_NAME = "refresh";
    public static final String ACCOUNT_ID_ATTR_NAME = "accountId";
    public static final String DEFAULT_ENCODING = "UTF-8";
}
