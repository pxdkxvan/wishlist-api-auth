package com.pxkdxvan.auth.oauth2.provider;

import com.pxkdxvan.auth.oauth2.exception.InsufficientOAuth2UserDataException;
import com.pxkdxvan.auth.oauth2.exception.MatchingEmailNotFoundException;
import com.pxkdxvan.auth.oauth2.factory.OAuth2DTOFactory;
import com.pxkdxvan.auth.oauth2.factory.RequestEntityFactory;
import com.pxkdxvan.auth.oauth2.dto.GitHubEmailResponseDTO;
import com.pxkdxvan.auth.oauth2.dto.OAuth2AccountData;
import com.pxkdxvan.auth.shared.model.Provider;
import com.pxkdxvan.auth.shared.utils.OptionalHelper;

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

    GitHubProvider(OAuth2DTOFactory oAuth2DTOFactory, RequestEntityFactory requestEntityFactory, RestOperations restOperations) {
        super(Provider.GITHUB, OAuth2ProviderAttrs.GITHUB, oAuth2DTOFactory);
        this.requestEntityFactory = requestEntityFactory;
        this.restOperations = restOperations;
    }

    protected String fetchPrimaryEmail(String accessToken) {
        RequestEntity<Void> request = requestEntityFactory
                .createGitHubEmailFetch(FETCH_EMAILS_URL, accessToken);

        ResponseEntity<List<GitHubEmailResponseDTO>> responseDTO = restOperations
                .exchange(request, ParameterizedTypeReference.forType(List.class));

        return Stream
                .ofNullable(responseDTO.getBody())
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
        OAuth2DTOFactory oAuth2DTOFactory = getOAuth2DTOFactory();
        OAuth2ProviderAttrs oAuth2ProviderAttrs = getOAuth2ProviderAttrs();

        String username = extractUsername(oAuth2User);
        String email = extractEmail(oAuth2User, accessToken);
        String providerId = extractProperty(oAuth2User, oAuth2ProviderAttrs.getProviderIdAttr());

        return oAuth2DTOFactory.create(username, email, providerId);
    }

}
