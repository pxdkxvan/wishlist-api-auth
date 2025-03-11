package com.pxkdxvan.auth.shared.service;

import com.pxkdxvan.auth.shared.factory.AuthorizationFactory;
import com.pxkdxvan.auth.shared.model.Account;
import com.pxkdxvan.auth.shared.model.Authorization;
import com.pxkdxvan.auth.shared.model.Provider;
import com.pxkdxvan.auth.shared.repository.AuthorizationRepository;

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
