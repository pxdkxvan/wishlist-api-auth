package com.pxdkxvan.auth.controller;

import com.pxdkxvan.auth.config.ConstantsConfig;
import com.pxdkxvan.auth.dto.TokenResponseDTO;
import com.pxdkxvan.auth.response.ResponseCode;
import com.pxdkxvan.auth.response.TokenResponseShaper;
import com.pxdkxvan.auth.dto.ResponseDTO;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/oauth2/login")
@RequiredArgsConstructor
public class OAuth2AuthController {

    private final TokenResponseShaper tokenResponseShaper;

    @GetMapping("/callback")
    public ResponseEntity<ResponseDTO<TokenResponseDTO>> callback(@AuthenticationPrincipal OAuth2User oAuth2User) {
        UUID accountId = oAuth2User.getAttribute(ConstantsConfig.ACCOUNT_ID_ATTR_NAME);
        return tokenResponseShaper.buildDual(ResponseCode.ACCOUNT_LOGGED_IN, String.valueOf(accountId));
    }

}
