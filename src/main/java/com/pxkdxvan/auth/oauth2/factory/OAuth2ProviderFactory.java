package com.pxkdxvan.auth.oauth2.factory;

import com.pxkdxvan.auth.oauth2.provider.DefaultOAuth2Provider;
import com.pxkdxvan.auth.oauth2.provider.OAuth2ProviderAttrs;
import com.pxkdxvan.auth.shared.model.Provider;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public final class OAuth2ProviderFactory {

    private final OAuth2DTOFactory oAuth2DTOFactory;

    public DefaultOAuth2Provider createDefault(Provider provider, OAuth2ProviderAttrs oAuth2ProviderAttrs) {
        return new DefaultOAuth2Provider(provider, oAuth2ProviderAttrs, oAuth2DTOFactory);
    }

}
