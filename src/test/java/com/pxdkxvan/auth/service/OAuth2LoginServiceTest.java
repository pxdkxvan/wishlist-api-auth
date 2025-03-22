package com.pxdkxvan.auth.service;

import com.pxdkxvan.auth.support.factory.TestEntityFactory;
import com.pxdkxvan.auth.exception.provider.ProviderNotFoundException;
import com.pxdkxvan.auth.factory.OAuth2UserFactory;
import com.pxdkxvan.auth.factory.OAuth2ProviderFactory;
import com.pxdkxvan.auth.model.Account;
import com.pxdkxvan.auth.model.Authorization;
import com.pxdkxvan.auth.model.OAuth2AccountData;
import com.pxdkxvan.auth.model.Provider;
import com.pxdkxvan.auth.provider.DefaultOAuth2Provider;
import com.pxdkxvan.auth.provider.GitHubProvider;
import com.pxdkxvan.auth.provider.GoogleProvider;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.util.Collections;
import java.util.UUID;

import static com.pxdkxvan.auth.support.config.TestConstants.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import static org.mockito.Mockito.*;

@RequiredArgsConstructor(onConstructor_ = @Autowired, access = AccessLevel.PACKAGE)
@SpringJUnitConfig(classes = {OAuth2LoginService.class, OAuth2UserFactory.class, TestEntityFactory.class})
class OAuth2LoginServiceTest {

    @MockitoBean
    private final GoogleProvider googleProvider;
    @MockitoBean
    private final GitHubProvider gitHubProvider;

    @MockitoBean
    private final AccountService accountService;
    @MockitoBean
    private final AuthorizationService authorizationService;
    private final OAuth2LoginService oAuth2LoginService;

    @MockitoBean
    private final OAuth2ProviderFactory oAuth2ProviderFactory;
    private final TestEntityFactory testEntityFactory;

    @ParameterizedTest
    @EnumSource(value = Provider.class, mode = EnumSource.Mode.EXCLUDE, names = {"LOCAL"})
    void loadOAuth2UserSuccess(Provider provider) {
        OAuth2UserRequest testUserRequest = mock(OAuth2UserRequest.class);
        OAuth2User testOAuth2User = mock(OAuth2User.class);

        OAuth2AccessToken testAccessToken = mock(OAuth2AccessToken.class);
        when(testUserRequest.getAccessToken()).thenReturn(testAccessToken);
        when(testAccessToken.getTokenValue()).thenReturn(OAUTH2_ACCESS_TOKEN);

        ClientRegistration testOAuth2ClientRegistration = mock(ClientRegistration.class);
        when(testUserRequest.getClientRegistration()).thenReturn(testOAuth2ClientRegistration);
        when(testOAuth2ClientRegistration.getRegistrationId()).thenReturn(provider.name());

        OAuth2AccountData testOAuth2AccountData = testEntityFactory.createOAuth2AccountData();
        when(googleProvider.getAccountData(testOAuth2User)).thenReturn(testOAuth2AccountData);
        when(gitHubProvider.getAccountData(testOAuth2User, OAUTH2_ACCESS_TOKEN)).thenReturn(testOAuth2AccountData);

        DefaultOAuth2Provider testDefaultOAuth2Provider = mock(DefaultOAuth2Provider.class);
        when(oAuth2ProviderFactory.createDefault(provider)).thenReturn(testDefaultOAuth2Provider);
        when(testDefaultOAuth2Provider.getAccountData(testOAuth2User)).thenReturn(testOAuth2AccountData);

        Authorization testAuthorization = testEntityFactory.createIdentifiedAuthorization(provider);
        Account testAccount = testAuthorization.getAccount();
        UUID testAccountId = testAccount.getId();
        Boolean verified = testAccount.getVerified();

        when(accountService.getOrCreateDefaultAccount(ACCOUNT_USERNAME, ACCOUNT_EMAIL)).thenReturn(testAccount);
        when(authorizationService.getOrCreateOAuth2Auth(testAccount, provider, AUTHORIZATION_PROVIDER_ID)).thenReturn(testAuthorization);

        when(testOAuth2User.getAttributes()).thenReturn(Collections.emptyMap());

        OAuth2User modifiedOAuth2User = oAuth2LoginService.loadUser(testUserRequest, testOAuth2User);
        assertThat(modifiedOAuth2User.getAuthorities()).hasSize(1);
        assertThat(modifiedOAuth2User.getAttributes()).hasSize(1);
        assertThat(modifiedOAuth2User.getName()).isEqualTo(String.valueOf(testAccountId));

        verify(testUserRequest).getAccessToken();
        verify(testAccessToken).getTokenValue();

        verify(testUserRequest).getClientRegistration();
        verify(testOAuth2ClientRegistration).getRegistrationId();

        switch (provider) {
            case GOOGLE -> verify(googleProvider).getAccountData(testOAuth2User);
            case GITHUB -> verify(gitHubProvider).getAccountData(testOAuth2User, OAUTH2_ACCESS_TOKEN);
            default -> {
                verify(oAuth2ProviderFactory).createDefault(provider);
                verify(testDefaultOAuth2Provider).getAccountData(testOAuth2User);
            }
        }

        verify(accountService).getOrCreateDefaultAccount(ACCOUNT_USERNAME, ACCOUNT_EMAIL);
        if (!verified) verify(accountService).verifyAccount(testAccount);
        verify(authorizationService).getOrCreateOAuth2Auth(testAccount, provider, AUTHORIZATION_PROVIDER_ID);

        verify(testOAuth2User).getAttributes();
    }

    @Test
    void loadOauth2UserFailedProviderNotFound() {
        Provider provider = Provider.LOCAL;

        OAuth2UserRequest testUserRequest = mock(OAuth2UserRequest.class);
        OAuth2User testOAuth2User = mock(OAuth2User.class);

        OAuth2AccessToken testAccessToken = mock(OAuth2AccessToken.class);
        when(testUserRequest.getAccessToken()).thenReturn(testAccessToken);
        when(testAccessToken.getTokenValue()).thenReturn(OAUTH2_ACCESS_TOKEN);

        ClientRegistration testOAuth2ClientRegistration = mock(ClientRegistration.class);
        when(testUserRequest.getClientRegistration()).thenReturn(testOAuth2ClientRegistration);
        when(testOAuth2ClientRegistration.getRegistrationId()).thenReturn(provider.name());

        assertThatThrownBy(() -> oAuth2LoginService.loadUser(testUserRequest, testOAuth2User))
                .isInstanceOf(ProviderNotFoundException.class)
                .extracting(e -> ((ProviderNotFoundException) e).getProvider())
                .isEqualTo(provider);

        verify(testUserRequest).getAccessToken();
        verify(testAccessToken).getTokenValue();

        verify(testUserRequest).getClientRegistration();
        verify(testOAuth2ClientRegistration).getRegistrationId();
    }

}
