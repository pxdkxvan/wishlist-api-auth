package com.pxkdxvan.auth.oauth2.adapter;

import com.pxkdxvan.auth.oauth2.service.OAuth2LoginService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public class OAuth2LoginAdapter implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final DefaultOAuth2UserService defaultOAuth2UserService;
    private final OAuth2LoginService oAuth2LoginService;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) {
        OAuth2User oAuth2User = defaultOAuth2UserService.loadUser(userRequest);
        return oAuth2LoginService.loadUser(userRequest, oAuth2User);
    }

}
