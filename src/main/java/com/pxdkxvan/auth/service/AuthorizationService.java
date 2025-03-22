package com.pxdkxvan.auth.service;

import com.pxdkxvan.auth.factory.AuthorizationFactory;
import com.pxdkxvan.auth.model.Account;
import com.pxdkxvan.auth.model.Authorization;
import com.pxdkxvan.auth.model.Provider;
import com.pxdkxvan.auth.repository.AuthorizationRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public class AuthorizationService {

    private final AuthorizationRepository authorizationRepository;

    private final AuthorizationFactory authorizationFactory;

    @Transactional
    public Authorization createLocalAuth(Account account, String password) {
        Authorization newAuth = authorizationFactory.createLocal(account, password);
        account.bindAuthorization(newAuth);
        return authorizationRepository.save(newAuth);
    }

    @Transactional
    public Authorization createOAuth2Auth(Account account, Provider provider, String providerId) {
        Authorization newAuth = authorizationFactory.createOAuth2(account, provider, providerId);
        account.bindAuthorization(newAuth);
        return authorizationRepository.save(newAuth);
    }

    @Transactional
    public Authorization getOrCreateOAuth2Auth(Account account, Provider provider, String providerId) {
        return authorizationRepository
                .findByAccountAndProvider(account, provider)
                .orElseGet(() -> createOAuth2Auth(account, provider, providerId));
    }

}
