package com.pxkdxvan.auth.local.controller;

import com.pxkdxvan.auth.local.model.VerificationMethod;
import com.pxkdxvan.auth.local.service.EmailService;
import com.pxkdxvan.auth.shared.builder.SimpleResponseBuilder;
import com.pxkdxvan.auth.shared.dto.ResponseCode;
import com.pxkdxvan.auth.shared.dto.ResponseDTO;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth/email/send")
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public class EmailController {

    private final SimpleResponseBuilder simpleResponseBuilder;

    private final EmailService emailService;

    @GetMapping("/link")
    public ResponseEntity<ResponseDTO<Void>> sendLinkVerificationEmail(@AuthenticationPrincipal Jwt jwt) {
        String jwtValue = jwt.getTokenValue();
        emailService.sendVerificationEmail(VerificationMethod.LINK, jwtValue);
        return simpleResponseBuilder.buildResponse(HttpStatus.OK, ResponseCode.VERIFICATION_EMAIL_SENT);
    }

    @GetMapping("/code")
    public ResponseEntity<ResponseDTO<Void>> sentCodeVerificationEmail(@AuthenticationPrincipal Jwt jwt) {
        String jwtValue = jwt.getTokenValue();
        emailService.sendVerificationEmail(VerificationMethod.CODE, jwtValue);
        return simpleResponseBuilder.buildResponse(HttpStatus.OK, ResponseCode.VERIFICATION_EMAIL_SENT);
    }

}
