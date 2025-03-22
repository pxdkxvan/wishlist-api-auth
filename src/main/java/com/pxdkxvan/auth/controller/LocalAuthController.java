package com.pxdkxvan.auth.controller;

import com.pxdkxvan.auth.dto.LoginRequestDTO;
import com.pxdkxvan.auth.dto.RegisterRequestDTO;
import com.pxdkxvan.auth.dto.TokenResponseDTO;
import com.pxdkxvan.auth.service.LocalAuthService;
import com.pxdkxvan.auth.config.ConstantsConfig;
import com.pxdkxvan.auth.response.ResponseCode;
import com.pxdkxvan.auth.dto.ResponseDTO;
import com.pxdkxvan.auth.model.Account;
import com.pxdkxvan.auth.security.JwtType;
import com.pxdkxvan.auth.service.JwtService;
import com.pxdkxvan.auth.response.TokenResponseShaper;

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

    private final TokenResponseShaper tokenResponseShaper;

    @PostMapping("/login")
    public ResponseEntity<ResponseDTO<TokenResponseDTO>> login(@RequestBody @Valid LoginRequestDTO req) {
        Account authorized = localAuthService.login(req.getLogin(), req.getPassword());
        return tokenResponseShaper.buildDual(ResponseCode.ACCOUNT_LOGGED_IN, String.valueOf(authorized.getId()));
    }

    @PostMapping("/register")
    public ResponseEntity<ResponseDTO<TokenResponseDTO>> register(@RequestBody @Valid RegisterRequestDTO req) {
        Account registered = localAuthService.register(req.getUsername(), req.getEmail(), req.getPassword());
        return tokenResponseShaper.buildSingle(ResponseCode.ACCOUNT_CREATED, JwtType.TEMPORARY, registered.getId().toString());
    }

    @PostMapping("/refresh")
    public ResponseEntity<ResponseDTO<TokenResponseDTO>> refresh(@Valid @CookieValue(ConstantsConfig.REFRESH_COOKIE_NAME) String refreshToken) {
        String accountId = jwtService.extractSubject(refreshToken);
        return tokenResponseShaper.buildSingle(ResponseCode.TOKEN_REFRESHED, JwtType.ACCESS, accountId);
    }

}
