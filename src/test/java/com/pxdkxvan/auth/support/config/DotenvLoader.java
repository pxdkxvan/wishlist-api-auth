package com.pxdkxvan.auth.support.config;

import io.github.cdimascio.dotenv.Dotenv;
import io.github.cdimascio.dotenv.DotenvEntry;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Map;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class DotenvLoader {
    public static <T> Map<String, T> loadEnv(Class<T> valueType) {
        return Dotenv
                .configure()
                .ignoreIfMissing()
                .load()
                .entries()
                .stream()
                .collect(Collectors.toMap(DotenvEntry::getKey, e -> valueType.cast(e.getValue())));
    }
}
