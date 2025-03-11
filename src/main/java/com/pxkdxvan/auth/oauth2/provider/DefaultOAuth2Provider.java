package com.pxkdxvan.auth.oauth2.provider;

import com.pxkdxvan.auth.oauth2.dto.OAuth2AccountData;
import com.pxkdxvan.auth.oauth2.factory.OAuth2DTOFactory;
import com.pxkdxvan.auth.shared.exception.PropertyNotFoundException;
import com.pxkdxvan.auth.shared.model.Provider;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Optional;

@Getter(AccessLevel.PROTECTED)
@RequiredArgsConstructor
public class DefaultOAuth2Provider {

    @Getter(AccessLevel.PUBLIC)
    private final Provider provider;

    private final OAuth2ProviderAttrs oAuth2ProviderAttrs;
    private final OAuth2DTOFactory oAuth2DTOFactory;

    protected String extractProperty(OAuth2User user, String name) {
        return Optional
                .ofNullable(user.getAttribute(name))
                .map(Object::toString)
                .orElseThrow(() -> new PropertyNotFoundException("Failed to extract OAuth2 user property", name));
    }

    protected String extractUsername(OAuth2User user) {
        return extractProperty(user, oAuth2ProviderAttrs.getUsernameAttr());
    }

    protected String extractEmail(OAuth2User user) {
        return extractProperty(user, oAuth2ProviderAttrs.getEmailAttr());
    }

    public OAuth2AccountData getAccountData(OAuth2User oAuth2User) {
        String username = extractUsername(oAuth2User);
        String email = extractEmail(oAuth2User);
        String providerId = extractProperty(oAuth2User, oAuth2ProviderAttrs.getProviderIdAttr());
        return oAuth2DTOFactory.create(username, email, providerId);
    }

}
