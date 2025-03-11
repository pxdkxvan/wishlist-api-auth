package com.pxkdxvan.auth.local.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.captcha")
public record CaptchaProperties(String verifyLink, Key key) {
    public record Key(String site, String secret) {}
}
