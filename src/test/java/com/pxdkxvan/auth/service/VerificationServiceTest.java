package com.pxdkxvan.auth.service;

import com.pxdkxvan.auth.support.factory.TestEntityFactory;
import com.pxdkxvan.auth.exception.verification.VerificationKeyExpiredException;
import com.pxdkxvan.auth.exception.verification.VerificationNotFoundException;
import com.pxdkxvan.auth.model.Account;
import com.pxdkxvan.auth.model.Verification;
import com.pxdkxvan.auth.model.VerificationMethod;
import com.pxdkxvan.auth.properties.VerificationProperties;
import com.pxdkxvan.auth.repository.VerificationRepository;
import com.pxdkxvan.auth.support.util.TestRandomizer;
import com.pxdkxvan.auth.util.TimeUtils;
import com.pxdkxvan.auth.generator.VerificationGenerator;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.ConfigDataApplicationContextInitializer;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

import static com.pxdkxvan.auth.support.config.TestConstants.VERIFICATION_EXPIRES_AT;
import static com.pxdkxvan.auth.support.config.TestConstants.VERIFICATION_TOKEN;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import static org.mockito.Mockito.*;

@EnableConfigurationProperties(VerificationProperties.class)
@RequiredArgsConstructor(onConstructor_ = @Autowired, access = AccessLevel.PACKAGE)
@SpringJUnitConfig(classes = {VerificationService.class, TestEntityFactory.class, TestRandomizer.class},
        initializers = ConfigDataApplicationContextInitializer.class)
class VerificationServiceTest {

    @MockitoBean
    private final VerificationGenerator verificationGenerator;

    @MockitoBean
    private final VerificationRepository verificationRepository;

    @MockitoBean
    private final AccountService accountService;
    private final VerificationService verificationService;

    private final TestEntityFactory testEntityFactory;

    private final TestRandomizer testRandomizer;

    static Stream<Arguments> generateVerificationKeyArgs() {
        return Stream.of(
                Arguments.of(VerificationMethod.LINK, false),
                Arguments.of(VerificationMethod.LINK, true),
                Arguments.of(VerificationMethod.CODE, false),
                Arguments.of(VerificationMethod.CODE, true)
        );
    }

    @ParameterizedTest
    @MethodSource("generateVerificationKeyArgs")
    void generateVerificationKeySuccess(VerificationMethod method, boolean exists) {
        Verification testVerification = testEntityFactory.createVerification(method);
        Account testAccount = testVerification.getAccount();
        String testKey = testVerification.getKey();

        when(verificationGenerator.random(method)).thenReturn(testKey);

        Verification found = exists ? testVerification : null;
        when(verificationRepository.findByMethodAndAccount(method, testAccount)).thenReturn(Optional.ofNullable(found));
        when(verificationRepository.save(any(Verification.class))).thenReturn(testVerification);

        assertThat(verificationService.generateVerificationKey(method, testAccount, VERIFICATION_EXPIRES_AT)).isEqualTo(testKey);

        verify(verificationGenerator).random(method);

        verify(verificationRepository).findByMethodAndAccount(method, testAccount);
        verify(verificationRepository).save(any(Verification.class));
    }

    @ParameterizedTest
    @EnumSource(VerificationMethod.class)
    void verifyAccountSuccess(VerificationMethod method) {
        Verification testVerification = testEntityFactory.createIdentifiedVerification(method);
        Account testAccount = testVerification.getAccount();
        UUID testAccountId = testAccount.getId();
        String testKey = testVerification.getKey();
        testVerification.setExpiresAt(TimeUtils.MAX);

        switch (method) {
            case LINK -> {
                when(verificationRepository.findByKey(testKey)).thenReturn(Optional.of(testVerification));
                verificationService.verifyAccountWithToken(testKey);
                verify(verificationRepository).findByKey(testKey);
            }
            case CODE -> {
                when(verificationRepository.findByKeyAndAccountId(testKey, testAccountId)).thenReturn(Optional.of(testVerification));
                verificationService.verifyAccountWithCode(testKey, testAccountId);
                verify(verificationRepository).findByKeyAndAccountId(testKey, testAccountId);
            }
        }

        verify(accountService).verifyAccount(testAccount);
        verify(verificationRepository).delete(testVerification);
    }

    @Test
    void verifyAccountFailedTokenNotFound() {
        when(verificationRepository.findByKey(VERIFICATION_TOKEN)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> verificationService.verifyAccountWithToken(VERIFICATION_TOKEN))
                .isInstanceOf(VerificationNotFoundException.class);

        verify(verificationRepository).findByKey(VERIFICATION_TOKEN);
    }

    @Test
    void verifyAccountFailedCodeNotFound() {
        UUID accountId = UUID.randomUUID();

        when(verificationRepository.findByKeyAndAccountId(VERIFICATION_TOKEN, accountId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> verificationService.verifyAccountWithCode(VERIFICATION_TOKEN, accountId))
                .isInstanceOf(VerificationNotFoundException.class);

        verify(verificationRepository).findByKeyAndAccountId(VERIFICATION_TOKEN, accountId);
    }

    @Test
    void verifyAccountFailedKeyExpired() {
        VerificationMethod method = testRandomizer.randomVerificationMethod();
        assertThatThrownBy(() -> verificationService.verifyAccount(testEntityFactory.createVerification(method)))
                .isInstanceOf(VerificationKeyExpiredException.class);
    }

}
