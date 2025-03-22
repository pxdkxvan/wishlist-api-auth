package com.pxdkxvan.auth.service;

import com.pxdkxvan.auth.support.factory.TestEntityFactory;
import com.pxdkxvan.auth.model.Account;
import com.pxdkxvan.auth.model.Authorization;
import com.pxdkxvan.auth.model.Provider;
import com.pxdkxvan.auth.repository.AuthorizationRepository;
import com.pxdkxvan.auth.support.util.TestRandomizer;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.util.Optional;

import static com.pxdkxvan.auth.support.config.TestConstants.ACCOUNT_PASSWORD;
import static com.pxdkxvan.auth.support.config.TestConstants.AUTHORIZATION_PROVIDER_ID;

import static org.assertj.core.api.Assertions.assertThat;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RequiredArgsConstructor(onConstructor_ = @Autowired, access = AccessLevel.PACKAGE)
@SpringJUnitConfig(classes = {AuthorizationService.class, TestEntityFactory.class, TestRandomizer.class})
class AuthorizationServiceTest {

    @MockitoBean
    private final AuthorizationRepository authorizationRepository;
    private final AuthorizationService authorizationService;
    private final TestEntityFactory testEntityFactory;
    private final TestRandomizer testRandomizer;

    @Test
    void createLocalAuthorizationSuccess() {
        Authorization testAuthorization = testEntityFactory.createAuthorization(Provider.LOCAL);
        Account testAccount = testAuthorization.getAccount();
        when(authorizationRepository.save(any(Authorization.class))).thenReturn(testAuthorization);
        assertThat(authorizationService.createLocalAuth(testAccount, ACCOUNT_PASSWORD)).isEqualTo(testAuthorization);
        verify(authorizationRepository).save(any(Authorization.class));
    }

    @Test
    void createOAuth2AuthorizationSuccess() {
        Provider provider = testRandomizer.randomOAuth2Provider();

        Authorization testAuthorization = testEntityFactory.createAuthorization(provider);
        Account testAccount = testAuthorization.getAccount();

        when(authorizationRepository.save(any(Authorization.class))).thenReturn(testAuthorization);
        assertThat(authorizationService.createOAuth2Auth(testAccount, provider, AUTHORIZATION_PROVIDER_ID)).isEqualTo(testAuthorization);
        verify(authorizationRepository).save(any(Authorization.class));
    }

    @Test
    void getOAuth2AuthorizationSuccess() {
        Provider provider = testRandomizer.randomOAuth2Provider();

        Authorization testAuthorization = testEntityFactory.createAuthorization(provider);
        Account testAccount = testAuthorization.getAccount();

        when(authorizationRepository.findByAccountAndProvider(testAccount, provider)).thenReturn(Optional.of(testAuthorization));
        assertThat(authorizationService.getOrCreateOAuth2Auth(testAccount, provider, AUTHORIZATION_PROVIDER_ID)).isEqualTo(testAuthorization);
        verify(authorizationRepository).findByAccountAndProvider(testAccount, provider);
    }

    @Test
    void notGetButCreateOAuth2AuthorizationSuccess() {
        Provider provider = testRandomizer.randomOAuth2Provider();

        Authorization testAuthorization = testEntityFactory.createAuthorization(provider);
        Account testAccount = testAuthorization.getAccount();

        when(authorizationRepository.findByAccountAndProvider(testAccount, provider)).thenReturn(Optional.empty());
        when(authorizationRepository.save(any(Authorization.class))).thenReturn(testAuthorization);

        assertThat(authorizationService.getOrCreateOAuth2Auth(testAccount, provider, AUTHORIZATION_PROVIDER_ID)).isEqualTo(testAuthorization);

        verify(authorizationRepository).findByAccountAndProvider(testAccount, provider);
        verify(authorizationRepository).save(any(Authorization.class));
    }

}
