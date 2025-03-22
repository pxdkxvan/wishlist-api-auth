package com.pxdkxvan.auth.service;

import com.pxdkxvan.auth.generator.UsernameGenerator;
import com.pxdkxvan.auth.support.factory.TestEntityFactory;
import com.pxdkxvan.auth.exception.auth.AccountNotFoundException;
import com.pxdkxvan.auth.model.Account;
import com.pxdkxvan.auth.model.Role;
import com.pxdkxvan.auth.repository.AccountRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.util.Optional;
import java.util.UUID;

import static com.pxdkxvan.auth.support.config.TestConstants.ACCOUNT_EMAIL;
import static com.pxdkxvan.auth.support.config.TestConstants.ACCOUNT_USERNAME;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RequiredArgsConstructor(onConstructor_ = @Autowired, access = AccessLevel.PACKAGE)
@SpringJUnitConfig(classes = {AccountService.class, UsernameGenerator.class, TestEntityFactory.class})
class AccountServiceTest {

    @MockitoBean
    private final AccountRepository accountRepository;

    @MockitoBean
    private final RoleService roleService;
    private final AccountService accountService;

    private final TestEntityFactory testEntityFactory;

    @ParameterizedTest
    @ValueSource(booleans = {true, false})
    void isTakenByUsernameSuccess(boolean taken) {
        when(accountRepository.existsByUsername(ACCOUNT_USERNAME)).thenReturn(taken);
        assertThat(accountService.isUsernameTaken(ACCOUNT_USERNAME)).isEqualTo(taken);
        verify(accountRepository).existsByUsername(ACCOUNT_USERNAME);
    }

    @ParameterizedTest
    @ValueSource(booleans = {true, false})
    void isTakenByEmailSuccess(boolean taken) {
        when(accountRepository.existsByEmail(ACCOUNT_EMAIL)).thenReturn(taken);
        assertThat(accountService.isEmailTaken(ACCOUNT_EMAIL)).isEqualTo(taken);
        verify(accountRepository).existsByEmail(ACCOUNT_EMAIL);
    }

    @Test
    void findAccountByIdSuccess() {
        Account testAccount = testEntityFactory.createIdentifiedAccount();
        UUID testId = testEntityFactory.createAccount().getId();
        when(accountRepository.findById(testId)).thenReturn(Optional.of(testAccount));
        assertThat(accountService.findAccountById(testId)).isEqualTo(testAccount);
        verify(accountRepository).findById(testId);
    }

    @Test
    void findAccountByIdFailedAccountNotFound() {
        UUID testId = UUID.randomUUID();

        when(accountRepository.findById(testId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> accountService.findAccountById(testId))
                .isInstanceOf(AccountNotFoundException.class)
                .extracting(e -> ((AccountNotFoundException) e).getIdentifier())
                .isEqualTo(String.valueOf(testId));

        verify(accountRepository).findById(testId);
    }

    @Test
    void createDefaultAccountSuccess() {
        Account testAccount = testEntityFactory.createAccount();
        Role testRole = testEntityFactory.createRole();

        when(roleService.getDefaultRole()).thenReturn(testRole);
        when(accountRepository.save(any(Account.class))).thenReturn(testAccount);

        assertThat(accountService.createDefaultAccount(ACCOUNT_USERNAME, ACCOUNT_EMAIL)).isEqualTo(testAccount);

        verify(roleService).getDefaultRole();
        verify(accountRepository).save(any(Account.class));
    }

    @Test
    void getDefaultAccountSuccess() {
        Account testAccount = testEntityFactory.createAccount();
        when(accountRepository.findByEmail(ACCOUNT_EMAIL)).thenReturn(Optional.of(testAccount));
        assertThat(accountService.getOrCreateDefaultAccount(ACCOUNT_USERNAME, ACCOUNT_EMAIL)).isEqualTo(testAccount);
        verify(accountRepository).findByEmail(ACCOUNT_EMAIL);
    }

    @Test
    void notGetButCreateDefaultAccountSuccess() {
        Account testAccount = testEntityFactory.createAccount();
        Role testRole = testEntityFactory.createRole();

        when(accountRepository.findByEmail(ACCOUNT_EMAIL)).thenReturn(Optional.empty());
        when(roleService.getDefaultRole()).thenReturn(testRole);
        when(accountRepository.save(any(Account.class))).thenReturn(testAccount);

        assertThat(accountService.getOrCreateDefaultAccount(ACCOUNT_USERNAME, ACCOUNT_EMAIL)).isEqualTo(testAccount);

        verify(accountRepository).findByEmail(ACCOUNT_EMAIL);
        verify(roleService).getDefaultRole();
        verify(accountRepository).save(any(Account.class));
    }

    @Test
    void verifyAccountSuccess() {
        Account testAccount = testEntityFactory.createAccount();
        accountService.verifyAccount(testAccount);
        assertThat(testAccount.getVerified()).isTrue();
    }

}
