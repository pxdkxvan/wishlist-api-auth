package com.pxdkxvan.auth.provider;

import com.pxdkxvan.auth.model.OAuth2AccountData;
import com.pxdkxvan.auth.factory.OAuth2DataFactory;
import com.pxdkxvan.auth.exception.PropertyNotFoundException;
import com.pxdkxvan.auth.model.Provider;

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
    private final OAuth2DataFactory oAuth2DataFactory;

    protected String extractProperty(OAuth2User oAuth2User, String name) {
        return Optional
                .ofNullable(oAuth2User.getAttribute(name))
                .map(Object::toString)
                .orElseThrow(() -> new PropertyNotFoundException("Failed to extract OAuth2 user property", name));
    }

    protected String extractUsername(OAuth2User oAuth2User) {
        return extractProperty(oAuth2User, oAuth2ProviderAttrs.getUsernameAttr());
    }

    protected String extractEmail(OAuth2User oAuth2User) {
        return extractProperty(oAuth2User, oAuth2ProviderAttrs.getEmailAttr());
    }

    public OAuth2AccountData getAccountData(OAuth2User oAuth2User) {
        String username = extractUsername(oAuth2User);
        String email = extractEmail(oAuth2User);
        String providerId = extractProperty(oAuth2User, oAuth2ProviderAttrs.getProviderIdAttr());
        return oAuth2DataFactory.create(username, email, providerId);
    }

}
