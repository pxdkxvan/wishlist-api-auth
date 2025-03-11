package com.pxkdxvan.auth.shared.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import org.fusesource.jansi.Ansi;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ColorOutput {
    public static String colorize(Object input, Ansi.Color color) {
        return Ansi
                .ansi()
                .fg(color)
                .a(input)
                .reset()
                .toString();
    }
}