package com.pxdkxvan.auth.controller;

import com.pxdkxvan.auth.model.VerificationMethod;
import com.pxdkxvan.auth.service.MailingService;
import com.pxdkxvan.auth.response.StatusResponseShaper;
import com.pxdkxvan.auth.response.ResponseCode;
import com.pxdkxvan.auth.dto.ResponseDTO;

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
public class MailingController {

    private final MailingService mailingService;

    private final StatusResponseShaper statusResponseShaper;

    @GetMapping("/link")
    public ResponseEntity<ResponseDTO<Void>> sendLinkVerificationEmail(@AuthenticationPrincipal Jwt jwt) {
        String jwtValue = jwt.getTokenValue();
        mailingService.sendVerificationEmail(VerificationMethod.LINK, jwtValue);
        return statusResponseShaper.buildResponse(HttpStatus.OK, ResponseCode.VERIFICATION_EMAIL_SENT);
    }

    @GetMapping("/code")
    public ResponseEntity<ResponseDTO<Void>> sentCodeVerificationEmail(@AuthenticationPrincipal Jwt jwt) {
        String jwtValue = jwt.getTokenValue();
        mailingService.sendVerificationEmail(VerificationMethod.CODE, jwtValue);
        return statusResponseShaper.buildResponse(HttpStatus.OK, ResponseCode.VERIFICATION_EMAIL_SENT);
    }

}
