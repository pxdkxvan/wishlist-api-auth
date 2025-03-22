package com.pxdkxvan.auth.service;

import com.pxdkxvan.auth.exception.auth.AccountNotFoundException;
import com.pxdkxvan.auth.factory.AccountFactory;
import com.pxdkxvan.auth.model.Account;
import com.pxdkxvan.auth.model.Role;
import com.pxdkxvan.auth.repository.AccountRepository;
import com.pxdkxvan.auth.generator.UsernameGenerator;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public class AccountService {

    private final RoleService roleService;
    private final AccountFactory accountFactory;
    private final AccountRepository accountRepository;
    private final UsernameGenerator usernameGenerator;

    public boolean isUsernameTaken(String username) {
        return accountRepository.existsByUsername(username);
    }

    public boolean isEmailTaken(String email) {
        return accountRepository.existsByEmail(email);
    }

    public Account findAccountById(UUID accountId) {
        return accountRepository
                .findById(accountId)
                .orElseThrow(() -> new AccountNotFoundException(String.valueOf(accountId)));
    }

    private Account createDefaultAccountSafely(String username, String email) {
        return isUsernameTaken(username) ?
                createDefaultAccountSafely(usernameGenerator.random(), email) :
                createDefaultAccount(username, email);
    }

    public Account createDefaultAccount(String username, String email) {
        Role defaultRole = roleService.getDefaultRole();
        Account newAccount = accountFactory.create(username, email, new HashSet<>(Set.of(defaultRole)));
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
