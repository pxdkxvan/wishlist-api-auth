package com.pxdkxvan.auth.controller;

import com.pxdkxvan.auth.config.ConstantsConfig;
import com.pxdkxvan.auth.dto.CodeRequestDTO;
import com.pxdkxvan.auth.response.StatusResponseShaper;
import com.pxdkxvan.auth.response.ResponseCode;
import com.pxdkxvan.auth.dto.ResponseDTO;
import com.pxdkxvan.auth.service.JwtService;
import com.pxdkxvan.auth.service.VerificationService;

import jakarta.validation.Valid;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

@RestController
@RequestMapping("/auth/email/verify")
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public class VerificationController {

    private final JwtService jwtService;
    private final VerificationService verificationService;

    private final StatusResponseShaper statusResponseShaper;

    @GetMapping("/link")
    public ResponseEntity<ResponseDTO<Void>> verifyLink(@RequestParam(ConstantsConfig.VERIFICATION_LINK_PARAM_NAME) String token) {
        token = URLDecoder.decode(token, StandardCharsets.UTF_8);
        verificationService.verifyAccountWithToken(token);
        return statusResponseShaper.buildResponse(HttpStatus.OK, ResponseCode.ACCOUNT_VERIFIED);
    }

    @PostMapping("/code")
    public ResponseEntity<ResponseDTO<Void>> verifyCode(@AuthenticationPrincipal Jwt jwt, @RequestBody @Valid CodeRequestDTO req) {
        UUID accountId = UUID.fromString(jwtService.extractSubject(jwt.getTokenValue()));
        verificationService.verifyAccountWithCode(req.getCode(), accountId);
        return statusResponseShaper.buildResponse(HttpStatus.OK, ResponseCode.ACCOUNT_VERIFIED);
    }

}
