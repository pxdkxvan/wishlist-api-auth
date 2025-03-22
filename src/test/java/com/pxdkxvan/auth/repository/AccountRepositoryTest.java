package com.pxdkxvan.auth.repository;

import com.pxdkxvan.auth.support.factory.TestEntityFactory;
import com.pxdkxvan.auth.model.Account;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.UUID;

import static com.pxdkxvan.auth.support.config.TestConstants.ACCOUNT_USERNAME;
import static com.pxdkxvan.auth.support.config.TestConstants.ACCOUNT_EMAIL;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(TestEntityFactory.class)
@RequiredArgsConstructor(onConstructor_ = @Autowired, access = AccessLevel.PACKAGE)
class AccountRepositoryTest {

    private final AccountRepository accountRepository;
    private final TestEntityFactory testEntityFactory;

    @Test
    void saveAndFindAccountSuccess() {
        Account acc = testEntityFactory.createAccount();
        acc.setId(null);

        UUID testId = accountRepository
                .save(acc)
                .getId();

        assertThat(accountRepository.findById(testId))
                .isPresent()
                .hasValueSatisfying(account -> assertThat(account.getId()).isEqualTo(testId));
    }

    @Test
    void findAccountFailedAccountNotFound() {
        assertThat(accountRepository.findById(UUID.randomUUID())).isNotPresent();
    }

    @Test
    void findByEmailSuccess() {
        UUID testId = accountRepository
                .save(testEntityFactory.createAccount())
                .getId();

        assertThat(accountRepository.findByEmail(ACCOUNT_EMAIL))
                .isPresent()
                .hasValueSatisfying(account -> assertThat(account.getId()).isEqualTo(testId));
    }

    @Test
    void setDefaultFieldsSuccess() {
        Account testAccount = accountRepository.save(testEntityFactory.createAccount());
        assertThat(testAccount.getVerified()).isFalse();
        assertThat(testAccount.getCreatedAt()).isNotNull();
        assertThat(testAccount.getAuthorizations()).isNotNull();
        assertThat(testAccount.getVerifications()).isNotNull();
    }

    @Test
    void checkExistenceSuccess() {
        accountRepository.save(testEntityFactory.createAccount());

        assertThat(accountRepository.existsByUsername(ACCOUNT_USERNAME)).isTrue();
        assertThat(accountRepository.existsByUsername(ACCOUNT_EMAIL)).isFalse();

        assertThat(accountRepository.existsByEmail(ACCOUNT_EMAIL)).isTrue();
        assertThat(accountRepository.existsByEmail(ACCOUNT_USERNAME)).isFalse();
    }

    @Test
    void deleteAccountSuccess() {
        UUID testId = accountRepository
                .save(testEntityFactory.createAccount())
                .getId();

        accountRepository.deleteById(testId);

        assertThat(accountRepository.findById(testId)).isNotPresent();
    }

}
