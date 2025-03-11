package com.pxkdxvan.auth.local.service;

import com.pxkdxvan.auth.local.exception.auth.*;
import com.pxkdxvan.auth.shared.mapper.AccountMapper;
import com.pxkdxvan.auth.shared.model.Account;
import com.pxkdxvan.auth.shared.model.Authorization;
import com.pxkdxvan.auth.shared.security.AccountDetails;
import com.pxkdxvan.auth.shared.service.AccountService;
import com.pxkdxvan.auth.local.utils.EmailMasker;
import com.pxkdxvan.auth.shared.service.AuthorizationService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

import org.springframework.security.authentication.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public class LocalAuthService {

    private final AccountService accountService;
    private final AuthorizationService authorizationService;

    private final AuthenticationManager authenticationManager;

    @Transactional
    public Account login(String login, String password) {
        return AccountMapper.toAccount(
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
