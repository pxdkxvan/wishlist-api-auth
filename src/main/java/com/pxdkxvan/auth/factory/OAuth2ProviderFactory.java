package com.pxdkxvan.auth.factory;

import com.pxdkxvan.auth.provider.DefaultOAuth2Provider;
import com.pxdkxvan.auth.provider.OAuth2ProviderAttrs;
import com.pxdkxvan.auth.model.Provider;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public final class OAuth2ProviderFactory {

    private final OAuth2DataFactory oAuth2DataFactory;

    public DefaultOAuth2Provider createDefault(Provider provider) {
        OAuth2ProviderAttrs attrs = OAuth2ProviderAttrs.valueOf(provider.name());
        return new DefaultOAuth2Provider(provider, attrs, oAuth2DataFactory);
    }

}
