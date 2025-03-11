package com.pxkdxvan.auth.shared.factory;

import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
public final class CookieFactory {

    private static final boolean DEFAULT_HTTP_ONLY = true;
    private static final boolean DEFAULT_SECURE = false;
    private static final String DEFAULT_PATH = "/";
    private static final Duration DEFAULT_EXPIRY = Duration.ofDays(7);

    public ResponseCookie create(String name, String content, boolean httpOnly,
                                        boolean secure, String path, Duration expiry) {
        return ResponseCookie
                .from(name, content)
                .httpOnly(httpOnly)
                .secure(secure)
                .path(path)
                .maxAge(expiry)
                .build();
    }

    public ResponseCookie createDefault(String name, String content) {
        return create(name, content, DEFAULT_HTTP_ONLY, DEFAULT_SECURE, DEFAULT_PATH, DEFAULT_EXPIRY);
    }

}
