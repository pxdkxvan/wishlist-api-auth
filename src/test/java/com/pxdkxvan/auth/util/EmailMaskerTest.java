package com.pxdkxvan.auth.util;

import com.pxdkxvan.auth.config.ConstantsConfig;

import org.junit.jupiter.api.Test;

import static com.pxdkxvan.auth.support.config.TestConstants.*;

import static org.assertj.core.api.Assertions.assertThat;

public class EmailMaskerTest {

    @Test
    public void shouldMaskEmailSuccess() {
        assertThat(EmailMasker.mask(ACCOUNT_EMAIL)).isEqualTo(ACCOUNT_MASKED_EMAIL);
    }

    @Test
    public void shouldMaskEmailNoAtSign() {
        assertThat(EmailMasker.mask(ACCOUNT_USERNAME)).isEqualTo(ACCOUNT_USERNAME);
    }

    @Test
    public void shouldMaskEmailLengthToShort() {
        String cutUsername = ACCOUNT_USERNAME.substring(0, ConstantsConfig.DEFAULT_MASK_SHOWN_SYMBOLS_AMOUNT);
        assertThat(EmailMasker.mask(cutUsername)).isEqualTo(cutUsername);
    }

}
