package com.pxkdxvan.auth.oauth2.provider;

import com.pxkdxvan.auth.oauth2.factory.OAuth2DTOFactory;
import com.pxkdxvan.auth.oauth2.utils.UsernameGenerator;
import com.pxkdxvan.auth.shared.model.Provider;

import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
public class GoogleProvider extends DefaultOAuth2Provider {

    GoogleProvider(OAuth2DTOFactory oAuth2DTOFactory) {
        super(Provider.GOOGLE, OAuth2ProviderAttrs.GOOGLE, oAuth2DTOFactory);
    }

    @Override
    protected String extractUsername(OAuth2User user) {
        return UsernameGenerator.random();
    }

}
