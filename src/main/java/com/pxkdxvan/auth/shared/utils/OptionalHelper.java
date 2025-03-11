package com.pxkdxvan.auth.shared.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Optional;
import java.util.function.Supplier;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class OptionalHelper {

    public static <T> Optional<T> attemptOrEmpty(Supplier<T> supplier) {
        try {
            return Optional.ofNullable(supplier.get());
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public static <T> T getOrCreate(T value, Supplier<T> supplier) {
        return Optional
                .ofNullable(value)
                .orElseGet(supplier);
    }

    public static <T> T getOrDefault(T value, T defaultValue) {
        return Optional
                .ofNullable(value)
                .orElse(defaultValue);
    }

    public static <T, X extends RuntimeException> T getOrThrow(T value, Supplier<X> supplier) {
        return Optional
                .ofNullable(value)
                .orElseThrow(supplier);
    }

}
