package com.pxdkxvan.auth.util;

import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import javax.crypto.SecretKey;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class KeyDecoder {
    public static SecretKey decode(String query) {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(query));
    }
}
