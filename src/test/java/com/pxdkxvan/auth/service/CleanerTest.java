package com.pxdkxvan.auth.service;

import com.pxdkxvan.auth.cleaner.AccountCleaner;
import com.pxdkxvan.auth.cleaner.VerificationCleaner;
import com.pxdkxvan.auth.properties.CleanerProperties;
import com.pxdkxvan.auth.properties.VerificationProperties;
import com.pxdkxvan.auth.repository.AccountRepository;
import com.pxdkxvan.auth.repository.VerificationRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.ConfigDataApplicationContextInitializer;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.time.ZonedDateTime;

import static org.assertj.core.api.Assertions.assertThat;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RequiredArgsConstructor(onConstructor_ = @Autowired, access = AccessLevel.PACKAGE)
@EnableConfigurationProperties({CleanerProperties.class, VerificationProperties.class})
@SpringJUnitConfig(classes = {AccountCleaner.class, VerificationCleaner.class}, initializers = ConfigDataApplicationContextInitializer.class)
class CleanerTest {

    @MockitoBean
    private final AccountRepository accountRepository;
    @MockitoBean
    private final VerificationRepository verificationRepository;

    private final AccountCleaner accountCleaner;
    private final VerificationCleaner verificationCleaner;

    @Test
    void cleanAccountSuccess() {
        when(accountRepository.deleteByVerifiedIsFalseAndCreatedAtBefore(any(ZonedDateTime.class))).thenReturn(1);
        assertThat(accountCleaner.cleanUnverifiedAccounts()).isEqualTo(1);
        verify(accountRepository).deleteByVerifiedIsFalseAndCreatedAtBefore(any(ZonedDateTime.class));
    }

    @Test
    void cleanVerificationSuccess() {
        when(verificationRepository.deleteByExpiresAtBefore(any(ZonedDateTime.class))).thenReturn(1);
        assertThat(verificationCleaner.cleanExpiredVerifications()).isEqualTo(1);
        verify(verificationRepository).deleteByExpiresAtBefore(any(ZonedDateTime.class));
    }

}
