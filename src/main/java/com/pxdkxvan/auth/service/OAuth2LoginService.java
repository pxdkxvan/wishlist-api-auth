package com.pxdkxvan.auth.service;

import com.pxdkxvan.auth.model.OAuth2AccountData;
import com.pxdkxvan.auth.exception.provider.ProviderNotFoundException;
import com.pxdkxvan.auth.factory.OAuth2ProviderFactory;
import com.pxdkxvan.auth.factory.OAuth2UserFactory;
import com.pxdkxvan.auth.provider.GitHubProvider;
import com.pxdkxvan.auth.provider.GoogleProvider;
import com.pxdkxvan.auth.config.ConstantsConfig;
import com.pxdkxvan.auth.mapper.RoleMapper;
import com.pxdkxvan.auth.model.Account;
import com.pxdkxvan.auth.model.Authorization;
import com.pxdkxvan.auth.model.Provider;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public class OAuth2LoginService {

    private final GoogleProvider googleProvider;
    private final GitHubProvider gitHubProvider;

    private final AccountService accountService;
    private final AuthorizationService authorizationService;

    private final OAuth2UserFactory oAuth2UserFactory;
    private final OAuth2ProviderFactory oAuth2ProviderFactory;

    @Transactional
    public OAuth2User loadUser(OAuth2UserRequest userRequest, OAuth2User oAuth2User) throws OAuth2AuthenticationException {
        String accessToken = userRequest.getAccessToken().getTokenValue();

        String possibleProvider = userRequest
                .getClientRegistration()
                .getRegistrationId()
                .toUpperCase();

        Provider provider = Provider.valueOf(possibleProvider);

        OAuth2AccountData accountData = switch (provider) {
            case GOOGLE -> googleProvider.getAccountData(oAuth2User);
            case GITHUB -> gitHubProvider.getAccountData(oAuth2User, accessToken);
            case YANDEX, GITLAB, DISCORD ->
                    oAuth2ProviderFactory
                        .createDefault(provider)
                        .getAccountData(oAuth2User);
            default -> throw new ProviderNotFoundException(provider);
        };

        String username = accountData.getUsername();
        String email = accountData.getEmail();
        Account account = accountService.getOrCreateDefaultAccount(username, email);
        if (!account.getVerified()) accountService.verifyAccount(account);

        String providerId = accountData.getProviderId();
        Authorization authorization = authorizationService.getOrCreateOAuth2Auth(account, provider, providerId);

        List<GrantedAuthority> roles = RoleMapper.toGrantedAuthorityList(account.getRoles());
        Map<String, Object> attributes = new HashMap<>(oAuth2User.getAttributes());
        attributes.put(ConstantsConfig.ACCOUNT_ID_ATTR_NAME, account.getId());

        return oAuth2UserFactory.createDefault(userRequest, roles, attributes, ConstantsConfig.ACCOUNT_ID_ATTR_NAME);
    }

}
