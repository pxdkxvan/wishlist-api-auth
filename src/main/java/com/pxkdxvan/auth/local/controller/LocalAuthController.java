package com.pxkdxvan.auth.local.controller;

import com.pxkdxvan.auth.local.dto.LoginRequestDTO;
import com.pxkdxvan.auth.local.dto.RegisterRequestDTO;
import com.pxkdxvan.auth.local.dto.TokenResponseDTO;
import com.pxkdxvan.auth.local.service.LocalAuthService;

import com.pxkdxvan.auth.shared.config.ConstantsConfig;
import com.pxkdxvan.auth.shared.dto.ResponseCode;
import com.pxkdxvan.auth.shared.dto.ResponseDTO;
import com.pxkdxvan.auth.shared.model.Account;
import com.pxkdxvan.auth.shared.security.JwtType;
import com.pxkdxvan.auth.shared.service.JwtService;
import com.pxkdxvan.auth.shared.builder.TokenResponseBuilder;

import jakarta.validation.Valid;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public class LocalAuthController {

    private final JwtService jwtService;
    private final LocalAuthService localAuthService;

    private final TokenResponseBuilder tokenResponseBuilder;

    @PostMapping("/login")
    public ResponseEntity<ResponseDTO<TokenResponseDTO>> login(@RequestBody @Valid LoginRequestDTO req) {
        Account authorized = localAuthService.login(req.getLogin(), req.getPassword());
        return tokenResponseBuilder.buildDual(authorized.getId().toString());
    }

    @PostMapping("/register")
    public ResponseEntity<ResponseDTO<TokenResponseDTO>> register(@RequestBody @Valid RegisterRequestDTO req) {
        Account registered = localAuthService.register(req.getUsername(), req.getEmail(), req.getPassword());
        return tokenResponseBuilder.buildSingle(ResponseCode.ACCOUNT_CREATED, JwtType.TEMPORARY, registered.getId().toString());
    }

    @PostMapping("/refresh")
    public ResponseEntity<ResponseDTO<TokenResponseDTO>> refresh(@CookieValue(ConstantsConfig.REFRESH_COOKIE_NAME) String refreshToken) {
        String accountId = jwtService.extractSubject(refreshToken);
        return tokenResponseBuilder.buildSingle(ResponseCode.ACCOUNT_TOKEN_REFRESHED, JwtType.ACCESS, accountId);
    }

}
