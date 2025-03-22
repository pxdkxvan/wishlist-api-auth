package com.pxdkxvan.auth.provider;

import com.pxdkxvan.auth.exception.provider.InsufficientOAuth2UserDataException;
import com.pxdkxvan.auth.exception.provider.MatchingEmailNotFoundException;
import com.pxdkxvan.auth.factory.OAuth2DataFactory;
import com.pxdkxvan.auth.factory.RequestEntityFactory;
import com.pxdkxvan.auth.dto.GitHubEmailResponseDTO;
import com.pxdkxvan.auth.model.OAuth2AccountData;
import com.pxdkxvan.auth.model.Provider;
import com.pxdkxvan.auth.util.OptionalHelper;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestOperations;

import java.util.List;
import java.util.stream.Stream;

@Service
public class GitHubProvider extends DefaultOAuth2Provider {

    private static final String FETCH_EMAILS_URL = "https://api.github.com/user/emails";

    private final RequestEntityFactory requestEntityFactory;
    private final RestOperations restOperations;

    GitHubProvider(OAuth2DataFactory oAuth2DataFactory, RequestEntityFactory requestEntityFactory, RestOperations restOperations) {
        super(Provider.GITHUB, OAuth2ProviderAttrs.GITHUB, oAuth2DataFactory);
        this.requestEntityFactory = requestEntityFactory;
        this.restOperations = restOperations;
    }

    String fetchPrimaryEmail(String accessToken) {
        RequestEntity<Void> request = requestEntityFactory.createGet(FETCH_EMAILS_URL, accessToken);

        ResponseEntity<List<GitHubEmailResponseDTO>> responseEntity =
                restOperations.exchange(request, new ParameterizedTypeReference<>() {});

        return Stream
                .ofNullable(responseEntity.getBody())
                .flatMap(List::stream)
                .filter(GitHubEmailResponseDTO::isPrimary)
                .map(GitHubEmailResponseDTO::getEmail)
                .findFirst()
                .orElseThrow(() -> new MatchingEmailNotFoundException("No primary email found", getProvider()));
    }

    private String extractEmail(OAuth2User oAuth2User, String accessToken) {
        return OptionalHelper
                .attemptOrEmpty(() -> extractEmail(oAuth2User))
                .orElseGet(() -> fetchPrimaryEmail(accessToken));
    }

    @Override
    public OAuth2AccountData getAccountData(OAuth2User oAuth2User) {
        throw new InsufficientOAuth2UserDataException(getProvider());
    }

    public OAuth2AccountData getAccountData(OAuth2User oAuth2User, String accessToken) {
        OAuth2DataFactory oAuth2DataFactory = getOAuth2DataFactory();
        OAuth2ProviderAttrs oAuth2ProviderAttrs = getOAuth2ProviderAttrs();

        String username = extractUsername(oAuth2User);
        String email = extractEmail(oAuth2User, accessToken);
        String providerId = extractProperty(oAuth2User, oAuth2ProviderAttrs.getProviderIdAttr());

        return oAuth2DataFactory.create(username, email, providerId);
    }

}
