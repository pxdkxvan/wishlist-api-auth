package com.pxkdxvan.auth.shared.service;

import com.pxkdxvan.auth.local.exception.auth.AccountNotFoundException;
import com.pxkdxvan.auth.shared.factory.AccountFactory;
import com.pxkdxvan.auth.shared.model.Account;
import com.pxkdxvan.auth.shared.model.Role;
import com.pxkdxvan.auth.shared.repository.AccountRepository;
import com.pxkdxvan.auth.oauth2.utils.UsernameGenerator;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.UUID;

@Service
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public class AccountService {

    private final RoleService roleService;

    private final AccountFactory accountFactory;

    private final AccountRepository accountRepository;

    public boolean isUsernameTaken(String username) {
        return accountRepository.existsByUsername(username);
    }

    public boolean isEmailTaken(String email) {
        return accountRepository.existsByEmail(email);
    }

    public Account findAccountById(String accountId) {
        return accountRepository
                .findById(UUID.fromString(accountId))
                .orElseThrow(() -> new AccountNotFoundException(accountId));
    }

    private Account createDefaultAccountSafely(String username, String email) {
        return isUsernameTaken(username) ?
                createDefaultAccountSafely(UsernameGenerator.random(), email) :
                createDefaultAccount(username, email);
    }

    public Account createDefaultAccount(String username, String email) {
        Role defaultrole = roleService.getDefaultRole();
        Account newAccount = accountFactory.create(username, email, Collections.singleton(defaultrole));
        return accountRepository.save(newAccount);
    }

    public Account getOrCreateDefaultAccount(String username, String email) {
        return accountRepository
                .findByEmail(email)
                .orElseGet(() -> createDefaultAccountSafely(username, email));
    }

    @Transactional
    public void verifyAccount(Account account) {
        account.setVerified(true);
    }

}
