package com.pxdkxvan.auth.repository;

import com.pxdkxvan.auth.support.factory.TestEntityFactory;
import com.pxdkxvan.auth.model.Account;
import com.pxdkxvan.auth.model.Authorization;
import com.pxdkxvan.auth.model.Provider;
import com.pxdkxvan.auth.support.util.TestRandomizer;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.concurrent.ThreadLocalRandom;

import static com.pxdkxvan.auth.support.config.TestConstants.ACCOUNT_USERNAME;
import static com.pxdkxvan.auth.support.config.TestConstants.ACCOUNT_EMAIL;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import({TestEntityFactory.class, TestRandomizer.class})
@RequiredArgsConstructor(onConstructor_ = @Autowired, access = AccessLevel.PACKAGE)
class AuthorizationRepositoryTest {

    private final AuthorizationRepository authorizationRepository;
    private final TestEntityFactory testEntityFactory;
    private final TestRandomizer testRandomizer;

    @ParameterizedTest
    @EnumSource(value = Provider.class)
    void saveAndFindAuthorizationSuccess(Provider provider) {
        Long testId = authorizationRepository
                .save(testEntityFactory.createAuthorization(provider))
                .getId();

        assertThat(authorizationRepository.findById(testId))
                .isPresent()
                .hasValueSatisfying(authorization -> assertThat(authorization.getId()).isEqualTo(testId));
    }

    @Test
    void findAuthorizationFailedAuthorizationNotFound() {
        assertThat(authorizationRepository.findById(ThreadLocalRandom.current().nextLong())).isNotPresent();
    }

    @Test
    void findByLoginAndProviderSuccess() {
        Provider provider = testRandomizer.randomProvider();

        Long testId = authorizationRepository
                .save(testEntityFactory.createAuthorization(provider))
                .getId();

        assertThat(authorizationRepository.findByLoginAndProvider(ACCOUNT_USERNAME, provider))
                .isPresent()
                .hasValueSatisfying(authorization -> assertThat(authorization.getId()).isEqualTo(testId));

        assertThat(authorizationRepository.findByLoginAndProvider(ACCOUNT_EMAIL, provider))
                .isPresent()
                .hasValueSatisfying(authorization -> assertThat(authorization.getId()).isEqualTo(testId));
    }

    @Test
    void findByAccountAndProviderSuccess() {
        Provider provider = testRandomizer.randomProvider();

        Authorization testAuthorization = authorizationRepository.save(testEntityFactory.createAuthorization(provider));
        Long testId = testAuthorization.getId();
        Account testAccount = testAuthorization.getAccount();

        assertThat(authorizationRepository.findByAccountAndProvider(testAccount, provider))
                .isPresent()
                .hasValueSatisfying(authorization -> assertThat(authorization.getId()).isEqualTo(testId));
    }

}
