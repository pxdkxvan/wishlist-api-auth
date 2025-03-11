package com.pxkdxvan.auth.oauth2.service;

import com.pxkdxvan.auth.oauth2.dto.OAuth2AccountData;
import com.pxkdxvan.auth.oauth2.exception.ProviderNotFoundException;
import com.pxkdxvan.auth.oauth2.factory.OAuth2ProviderFactory;
import com.pxkdxvan.auth.oauth2.factory.OAuth2UserFactory;
import com.pxkdxvan.auth.oauth2.provider.*;
import com.pxkdxvan.auth.shared.config.ConstantsConfig;
import com.pxkdxvan.auth.shared.mapper.RoleMapper;
import com.pxkdxvan.auth.shared.model.Account;
import com.pxkdxvan.auth.shared.model.Authorization;
import com.pxkdxvan.auth.shared.model.Provider;
import com.pxkdxvan.auth.shared.service.AccountService;
import com.pxkdxvan.auth.shared.service.AuthorizationService;

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
        OAuth2ProviderAttrs oAuth2ProviderAttrs = OAuth2ProviderAttrs.valueOf(possibleProvider);

        OAuth2AccountData accountData = switch (provider) {
            case GOOGLE -> googleProvider.getAccountData(oAuth2User);
            case GITHUB -> gitHubProvider.getAccountData(oAuth2User, accessToken);
            case YANDEX, GITLAB, DISCORD ->
                    oAuth2ProviderFactory
                        .createDefault(provider, oAuth2ProviderAttrs)
                        .getAccountData(oAuth2User);
            default -> throw new ProviderNotFoundException(provider);
        };

        String username = accountData.getUsername();
        String email = accountData.getEmail();
        Account account = accountService.getOrCreateDefaultAccount(username, email);
        if (!account.getVerified()) accountService.verifyAccount(account);

        String providerId = accountData.getProviderId();
        Authorization auth = authorizationService.getOrCreateOAuth2Auth(account, provider, providerId);

        List<GrantedAuthority> roles = RoleMapper.toGrantedAuthorityList(account.getRoles());
        Map<String, Object> attributes = new HashMap<>(oAuth2User.getAttributes());
        attributes.put(ConstantsConfig.ACCOUNT_ID_ATTR_NAME, account.getId().toString());

        return oAuth2UserFactory.createDefault(userRequest, roles, attributes, ConstantsConfig.ACCOUNT_ID_ATTR_NAME);
    }

}
