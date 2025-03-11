package com.pxkdxvan.auth.oauth2.controller;

import com.pxkdxvan.auth.shared.config.ConstantsConfig;
import com.pxkdxvan.auth.local.dto.TokenResponseDTO;
import com.pxkdxvan.auth.shared.builder.TokenResponseBuilder;
import com.pxkdxvan.auth.shared.dto.ResponseDTO;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/oauth2/login")
@RequiredArgsConstructor
public class OAuth2LoginController {

    private final TokenResponseBuilder tokenResponseBuilder;

    @GetMapping("/callback")
    public ResponseEntity<ResponseDTO<TokenResponseDTO>> callback(@AuthenticationPrincipal OAuth2User oAuth2User) {
        String accountId = oAuth2User.getAttribute(ConstantsConfig.ACCOUNT_ID_ATTR_NAME);
        return tokenResponseBuilder.buildDual(accountId);
    }

}
