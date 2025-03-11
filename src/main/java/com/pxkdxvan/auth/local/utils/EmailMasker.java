package com.pxkdxvan.auth.local.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class EmailMasker {

    private static final String HIDING_SYMBOL = "*";
    private static final int SHOWN_SYMBOLS_AMOUNT = 2;

    public static String mask(String query) {
        int atIndex = query.indexOf('@');
        if (atIndex < SHOWN_SYMBOLS_AMOUNT * 3) return query;
        String prefixPart = query.substring(0, SHOWN_SYMBOLS_AMOUNT);
        String hiddenPart = HIDING_SYMBOL.repeat(atIndex - SHOWN_SYMBOLS_AMOUNT * 2);
        String suffixPart = query.substring(atIndex - SHOWN_SYMBOLS_AMOUNT, atIndex);
        return prefixPart + hiddenPart + suffixPart + query.substring(atIndex);
    }

}
