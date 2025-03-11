package com.pxkdxvan.auth.local.controller;

import com.pxkdxvan.auth.local.dto.CodeRequestDTO;
import com.pxkdxvan.auth.shared.builder.SimpleResponseBuilder;
import com.pxkdxvan.auth.shared.dto.ResponseCode;
import com.pxkdxvan.auth.shared.dto.ResponseDTO;
import com.pxkdxvan.auth.local.service.VerificationService;

import jakarta.validation.Valid;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth/email/verify")
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public class VerificationController {

    private final VerificationService verificationService;

    private final SimpleResponseBuilder simpleResponseBuilder;

    @GetMapping("/link")
    public ResponseEntity<ResponseDTO<Void>> verifyLink(@RequestParam("token") String token) {
        verificationService.verifyAccount(token);
        return simpleResponseBuilder.buildResponse(HttpStatus.OK, ResponseCode.ACCOUNT_VERIFIED);
    }

    @PostMapping("/code")
    public ResponseEntity<ResponseDTO<Void>> verifyCode(@AuthenticationPrincipal Jwt jwt, @RequestBody @Valid CodeRequestDTO req) {
        verificationService.verifyAccount(req.getCode());
        return simpleResponseBuilder.buildResponse(HttpStatus.OK, ResponseCode.ACCOUNT_VERIFIED);
    }

}
