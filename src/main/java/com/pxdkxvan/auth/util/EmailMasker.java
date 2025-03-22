package com.pxdkxvan.auth.util;

import com.pxdkxvan.auth.config.ConstantsConfig;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class EmailMasker {

    private static final String HIDING_SYMBOL = "*";
    private static final int SHOWN_AMOUNT = ConstantsConfig.DEFAULT_MASK_SHOWN_SYMBOLS_AMOUNT;

    public static String mask(String query) {
        if (query.indexOf('@') == -1 || query.length() <= SHOWN_AMOUNT) return query;
        String prefixPart = query.substring(0, SHOWN_AMOUNT);
        String restPart = query.substring(SHOWN_AMOUNT);
        return prefixPart + restPart.replaceAll("[^.@]", HIDING_SYMBOL);
    }

}
