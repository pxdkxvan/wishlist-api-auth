package com.pxdkxvan.auth.adapter;

import com.pxdkxvan.auth.service.OAuth2LoginService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public class OidcLoginAdapter implements OAuth2UserService<OidcUserRequest, OidcUser> {

    private final OidcUserService oidcUserService;
    private final OAuth2LoginService oAuth2LoginService;

    @Override
    public OidcUser loadUser(OidcUserRequest userRequest) {
        OidcUser oidcUser = oidcUserService.loadUser(userRequest);
        return (OidcUser) oAuth2LoginService.loadUser(userRequest, oidcUser);
    }

}
