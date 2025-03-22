package com.pxdkxvan.auth.support.config;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ApiConstants {
    public static final String LOGIN_ENDPOINT = "/auth/login";
    public static final String REGISTER_ENDPOINT = "/auth/register";
    public static final String REFRESH_ENDPOINT = "/auth/refresh";

    public static final String MAILING_LINK_ENDPOINT = "/auth/email/send/link";
    public static final String MAILING_CODE_ENDPOINT = "/auth/email/send/code";

    public static final String OAUTH2_CALLBACK_ENDPOINT = "/oauth2/login/callback";

    public static final String VERIFICATION_LINK_ENDPOINT = "/auth/email/verify/link";
    public static final String VERIFICATION_CODE_ENDPOINT = "/auth/email/verify/code";
}
