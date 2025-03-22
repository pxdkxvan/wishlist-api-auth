package com.pxdkxvan.auth.provider;

import com.pxdkxvan.auth.factory.OAuth2DataFactory;
import com.pxdkxvan.auth.generator.UsernameGenerator;
import com.pxdkxvan.auth.model.Provider;

import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
public class GoogleProvider extends DefaultOAuth2Provider {

    private final UsernameGenerator usernameGenerator;

    GoogleProvider(OAuth2DataFactory oAuth2DataFactory, UsernameGenerator usernameGenerator) {
        super(Provider.GOOGLE, OAuth2ProviderAttrs.GOOGLE, oAuth2DataFactory);
        this.usernameGenerator = usernameGenerator;
    }

    @Override
    protected String extractUsername(OAuth2User oAuth2User) {
        return usernameGenerator.random();
    }

}
