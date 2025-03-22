package com.pxdkxvan.auth.repository;

import com.pxdkxvan.auth.support.factory.TestEntityFactory;
import com.pxdkxvan.auth.model.Account;
import com.pxdkxvan.auth.model.Verification;
import com.pxdkxvan.auth.model.VerificationMethod;
import com.pxdkxvan.auth.support.util.TestRandomizer;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

import org.junit.jupiter.api.Test;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import({TestEntityFactory.class, TestRandomizer.class})
@RequiredArgsConstructor(onConstructor_ = @Autowired, access = AccessLevel.PACKAGE)
class VerificationRepositoryTest {

    private final VerificationRepository verificationRepository;
    private final TestEntityFactory testEntityFactory;
    private final TestRandomizer testRandomizer;

    @ParameterizedTest
    @EnumSource(VerificationMethod.class)
    void saveAndFindVerificationSuccess(VerificationMethod method) {
        Long testId = verificationRepository
                .save(testEntityFactory.createVerification(method))
                .getId();

        assertThat(verificationRepository.findById(testId))
                .isPresent()
                .hasValueSatisfying(verification -> assertThat(verification.getId()).isEqualTo(testId));
    }

    @Test
    void findVerificationFailedVerificationNotFound() {
        assertThat(verificationRepository.findById(ThreadLocalRandom.current().nextLong())).isNotPresent();
    }

    @Test
    void findByKeyVerificationSuccess() {
        VerificationMethod method = testRandomizer.randomVerificationMethod();

        Verification testVerification = verificationRepository.save(testEntityFactory.createVerification(method));
        Long testId = testVerification.getId();

        assertThat(verificationRepository.findByKey(testVerification.getKey()))
                .isPresent()
                .hasValueSatisfying(verification -> assertThat(verification.getId()).isEqualTo(testId));
    }

    @Test
    void findByKeyAndAccountIdSuccess() {
        VerificationMethod method = testRandomizer.randomVerificationMethod();

        Verification testVerification = verificationRepository.save(testEntityFactory.createVerification(method));
        Long testId = testVerification.getId();
        String testKey = testVerification.getKey();
        UUID testAccountId = testVerification.getAccount().getId();

        assertThat(verificationRepository.findByKeyAndAccountId(testKey, testAccountId))
                .isPresent()
                .hasValueSatisfying(verification -> assertThat(verification.getId()).isEqualTo(testId));
    }

    @Test
    void findByMethodAndAccountVerificationSuccess() {
        VerificationMethod method = testRandomizer.randomVerificationMethod();

        Verification testVerification = verificationRepository.save(testEntityFactory.createVerification(method));
        Long testId = testVerification.getId();
        Account testAccount = testVerification.getAccount();

        assertThat(verificationRepository.findByMethodAndAccount(method, testAccount))
                .isPresent()
                .hasValueSatisfying(verification -> assertThat(verification.getId()).isEqualTo(testId));
    }

    @Test
    void deleteVerificationSuccess() {
        VerificationMethod method = testRandomizer.randomVerificationMethod();

        Long testId = verificationRepository
                .save(testEntityFactory.createVerification(method))
                .getId();

        verificationRepository.deleteById(testId);

        assertThat(verificationRepository.findById(testId)).isNotPresent();
    }

}
