package com.pxdkxvan.auth.service;

import com.pxdkxvan.auth.exception.auth.EmailAlreadyTakenException;
import com.pxdkxvan.auth.exception.auth.UsernameAlreadyTakenException;
import com.pxdkxvan.auth.mapper.AccountMapper;
import com.pxdkxvan.auth.model.Account;
import com.pxdkxvan.auth.model.Authorization;
import com.pxdkxvan.auth.security.AccountDetails;
import com.pxdkxvan.auth.util.EmailMasker;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

import org.springframework.security.authentication.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public class LocalAuthService {

    private final AccountMapper accountMapper;

    private final AuthenticationManager authenticationManager;

    private final AccountService accountService;
    private final AuthorizationService authorizationService;

    @Transactional
    public Account login(String login, String password) {
        return accountMapper.toAccount(
                (AccountDetails) authenticationManager
                        .authenticate(new UsernamePasswordAuthenticationToken(login, password))
                        .getPrincipal());
    }

    @Transactional
    public Account register(String username, String email, String password) {
        if (accountService.isUsernameTaken(username))
            throw new UsernameAlreadyTakenException(username);
        if (accountService.isEmailTaken(email))
            throw new EmailAlreadyTakenException(EmailMasker.mask(email));

        Account newAccount = accountService.createDefaultAccount(username, email);
        Authorization newAuth = authorizationService.createLocalAuth(newAccount, password);

        return newAccount;
    }

}
