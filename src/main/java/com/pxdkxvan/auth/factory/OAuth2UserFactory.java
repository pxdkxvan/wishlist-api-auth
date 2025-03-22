package com.pxdkxvan.auth.factory;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Map;

@Component
public final class OAuth2UserFactory {

    private DefaultOAuth2User createDefaultOAuth2(Collection<? extends GrantedAuthority> authorities,
                                                  Map<String, Object> attributes, String nameAttributeKey) {
        return new DefaultOAuth2User(authorities, attributes, nameAttributeKey);
    }

    private DefaultOidcUser createDefaultOidc(OidcUserRequest userRequest, Collection<? extends GrantedAuthority> authorities,
                                              Map<String, Object> attributes, String nameAttributeKey) {
        OidcIdToken idToken = userRequest.getIdToken();
        OidcUserInfo userInfo = new OidcUserInfo(attributes);
        return new DefaultOidcUser(authorities, idToken, userInfo, nameAttributeKey);
    }

    public OAuth2User createDefault(OAuth2UserRequest userRequest, Collection<? extends GrantedAuthority> authorities,
                                    Map<String, Object> attributes, String nameAttributeKey) {
        return (userRequest instanceof OidcUserRequest) ?
                createDefaultOidc((OidcUserRequest) userRequest, authorities, attributes, nameAttributeKey) :
                createDefaultOAuth2(authorities, attributes, nameAttributeKey);
    }

}
