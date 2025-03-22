package com.pxdkxvan.auth.service;

import com.pxdkxvan.auth.support.factory.TestRequestDTOFactory;
import com.pxdkxvan.auth.support.factory.TestEntityFactory;
import com.pxdkxvan.auth.dto.GitHubEmailResponseDTO;
import com.pxdkxvan.auth.exception.PropertyNotFoundException;
import com.pxdkxvan.auth.exception.provider.InsufficientOAuth2UserDataException;
import com.pxdkxvan.auth.exception.provider.MatchingEmailNotFoundException;
import com.pxdkxvan.auth.factory.OAuth2ProviderFactory;
import com.pxdkxvan.auth.factory.RequestEntityFactory;
import com.pxdkxvan.auth.model.OAuth2AccountData;
import com.pxdkxvan.auth.model.Provider;
import com.pxdkxvan.auth.provider.DefaultOAuth2Provider;
import com.pxdkxvan.auth.provider.GitHubProvider;
import com.pxdkxvan.auth.provider.GoogleProvider;
import com.pxdkxvan.auth.provider.OAuth2ProviderAttrs;
import com.pxdkxvan.auth.generator.UsernameGenerator;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.web.client.RestOperations;

import java.util.Collections;

import static com.pxdkxvan.auth.support.config.TestConstants.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import static org.mockito.Mockito.*;

@RequiredArgsConstructor(onConstructor_ = @Autowired, access = AccessLevel.PACKAGE)
@SpringJUnitConfig(classes = {GoogleProvider.class, GitHubProvider.class, OAuth2ProviderFactory.class,
        RequestEntityFactory.class, TestEntityFactory.class, TestRequestDTOFactory.class})
class ProviderTest {

    @MockitoBean
    private final RestOperations restOperations;

    @MockitoBean
    private final UsernameGenerator usernameGenerator;

    private final GoogleProvider googleProvider;
    private final GitHubProvider gitHubProvider;

    private final OAuth2ProviderFactory oAuth2ProviderFactory;
    private final TestEntityFactory testEntityFactory;
    private final TestRequestDTOFactory testRequestDTOFactory;

    @ParameterizedTest
    @EnumSource(value = Provider.class, mode = EnumSource.Mode.EXCLUDE, names = {"LOCAL", "GOOGLE", "GITHUB"})
    void getDefaultOAuth2AccountDataSuccess(Provider provider) {
        OAuth2User testOAuth2User = mock(OAuth2User.class);

        OAuth2ProviderAttrs testOauth2ProviderAttrs = OAuth2ProviderAttrs.valueOf(provider.name());
        when(testOAuth2User.getAttribute(testOauth2ProviderAttrs.getUsernameAttr())).thenReturn(ACCOUNT_USERNAME);
        when(testOAuth2User.getAttribute(testOauth2ProviderAttrs.getEmailAttr())).thenReturn(ACCOUNT_EMAIL);
        when(testOAuth2User.getAttribute(testOauth2ProviderAttrs.getProviderIdAttr())).thenReturn(AUTHORIZATION_PROVIDER_ID);

        OAuth2AccountData testOAuth2AccountData = testEntityFactory.createOAuth2AccountData();
        DefaultOAuth2Provider testDefaultProvider = oAuth2ProviderFactory.createDefault(provider);
        assertThat(testDefaultProvider.getAccountData(testOAuth2User)).isEqualTo(testOAuth2AccountData);

        verify(testOAuth2User, times(3)).getAttribute(any(String.class));
    }

    @Test
    void getGoogleAccountDataSuccess() {
        OAuth2User testOAuth2User = mock(OAuth2User.class);

        OAuth2ProviderAttrs testOauth2ProviderAttrs = OAuth2ProviderAttrs.GOOGLE;
        when(usernameGenerator.random()).thenReturn(ACCOUNT_USERNAME);
        when(testOAuth2User.getAttribute(testOauth2ProviderAttrs.getEmailAttr())).thenReturn(ACCOUNT_EMAIL);
        when(testOAuth2User.getAttribute(testOauth2ProviderAttrs.getProviderIdAttr())).thenReturn(AUTHORIZATION_PROVIDER_ID);

        OAuth2AccountData testOAuth2AccountData = testEntityFactory.createOAuth2AccountData();
        assertThat(googleProvider.getAccountData(testOAuth2User)).isEqualTo(testOAuth2AccountData);

        verify(testOAuth2User, times(2)).getAttribute(any(String.class));
    }

    @Test
    void getGitHubAccountDataSuccess() {
        OAuth2User testOAuth2User = mock(OAuth2User.class);

        OAuth2ProviderAttrs testOauth2ProviderAttrs = OAuth2ProviderAttrs.GITHUB;
        when(testOAuth2User.getAttribute(testOauth2ProviderAttrs.getUsernameAttr())).thenReturn(ACCOUNT_USERNAME);
        when(testOAuth2User.getAttribute(testOauth2ProviderAttrs.getEmailAttr())).thenReturn(ACCOUNT_EMAIL);
        when(testOAuth2User.getAttribute(testOauth2ProviderAttrs.getProviderIdAttr())).thenReturn(AUTHORIZATION_PROVIDER_ID);

        OAuth2AccountData testOAuth2AccountData = testEntityFactory.createOAuth2AccountData();
        assertThat(gitHubProvider.getAccountData(testOAuth2User, OAUTH2_ACCESS_TOKEN)).isEqualTo(testOAuth2AccountData);

        verify(testOAuth2User, times(3)).getAttribute(any(String.class));
    }

    @Test
    void notGetButFetchGitHubAccountDataSuccess() {
        OAuth2User testOAuth2User = mock(OAuth2User.class);

        OAuth2ProviderAttrs testOAuth2ProviderAttrs = OAuth2ProviderAttrs.GITHUB;
        String usernameAttr = testOAuth2ProviderAttrs.getUsernameAttr();
        String providerIdAttr = testOAuth2ProviderAttrs.getProviderIdAttr();

        when(testOAuth2User.getAttribute(usernameAttr)).thenReturn(ACCOUNT_USERNAME);
        when(testOAuth2User.getAttribute(providerIdAttr)).thenReturn(AUTHORIZATION_PROVIDER_ID);

        GitHubEmailResponseDTO testResponseDTO = testRequestDTOFactory.createGitHubEmailResponseDTO();
        when(restOperations.exchange(any(RequestEntity.class), any(ParameterizedTypeReference.class)))
                .thenReturn(ResponseEntity.ok(Collections.singletonList(testResponseDTO)));

        OAuth2AccountData oAuth2AccountData = testEntityFactory.createOAuth2AccountData();
        assertThat(gitHubProvider.getAccountData(testOAuth2User, OAUTH2_ACCESS_TOKEN)).isEqualTo(oAuth2AccountData);

        verify(testOAuth2User, times(3)).getAttribute(any(String.class));
        verify(restOperations).exchange(any(RequestEntity.class), any(ParameterizedTypeReference.class));
    }

    @Test
    void getOrFetchGitHubAccountDataFailedPrimaryEmailNotFound() {
        OAuth2User testOAuth2User = mock(OAuth2User.class);

        OAuth2ProviderAttrs testOAuth2ProviderAttrs = OAuth2ProviderAttrs.GITHUB;
        String usernameAttr = testOAuth2ProviderAttrs.getUsernameAttr();
        String providerIdAttr = testOAuth2ProviderAttrs.getProviderIdAttr();

        when(testOAuth2User.getAttribute(usernameAttr)).thenReturn(ACCOUNT_USERNAME);
        when(testOAuth2User.getAttribute(providerIdAttr)).thenReturn(AUTHORIZATION_PROVIDER_ID);

        when(restOperations.exchange(any(RequestEntity.class), any(ParameterizedTypeReference.class)))
                .thenReturn(ResponseEntity.ok(Collections.emptyList()));

        assertThatThrownBy(() -> gitHubProvider.getAccountData(testOAuth2User, OAUTH2_ACCESS_TOKEN))
                .isInstanceOf(MatchingEmailNotFoundException.class)
                .extracting(e -> ((MatchingEmailNotFoundException) e).getProvider())
                .isEqualTo(Provider.GITHUB);

        verify(testOAuth2User, times(2)).getAttribute(any(String.class));
        verify(restOperations).exchange(any(RequestEntity.class), any(ParameterizedTypeReference.class));
    }

    @Test
    void getOrFetchGitHubAccountDataFailedInsufficientOperation() {
        assertThatThrownBy(() -> gitHubProvider.getAccountData(mock(OAuth2User.class)))
                .isInstanceOf(InsufficientOAuth2UserDataException.class)
                .extracting(e -> ((InsufficientOAuth2UserDataException) e).getProvider())
                .isEqualTo(Provider.GITHUB);
    }

    @Test
    void getAccountDataFailedPropertyNotFound() {
        assertThatThrownBy(() -> googleProvider.getAccountData(mock(OAuth2User.class)))
                .isInstanceOf(PropertyNotFoundException.class)
                .extracting(e -> ((PropertyNotFoundException) e).getProperty())
                .isIn((Object[]) OAuth2ProviderAttrs.GOOGLE.getAttrs());
    }

}
