package com.pxdkxvan.auth.support.mapper;

import jakarta.servlet.http.Cookie;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import org.springframework.boot.test.context.TestComponent;
import org.springframework.http.ResponseCookie;

@TestComponent
@NoArgsConstructor(access = AccessLevel.PACKAGE)
public final class CookieMapper {
    public Cookie toCookie(ResponseCookie responseCookie) {
        Cookie cookie = new Cookie(responseCookie.getName(), responseCookie.getValue());
        cookie.setHttpOnly(responseCookie.isHttpOnly());
        cookie.setSecure(responseCookie.isSecure());
        cookie.setPath(responseCookie.getPath());
        cookie.setMaxAge((int) responseCookie.getMaxAge().getSeconds());
        cookie.setDomain(responseCookie.getDomain());
        return cookie;
    }
}
