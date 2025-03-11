package com.pxkdxvan.auth.local.service;

import com.pxkdxvan.auth.local.exception.mail.MailSendingException;
import com.pxkdxvan.auth.local.model.VerificationMethod;
import com.pxkdxvan.auth.local.utils.TimeConverter;
import com.pxkdxvan.auth.shared.model.Account;
import com.pxkdxvan.auth.shared.service.AccountService;
import com.pxkdxvan.auth.shared.service.JwtService;

import jakarta.mail.MessagingException;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;

@Service
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public class EmailService {

    private final JwtService jwtService;
    private final AccountService accountService;
    private final EmailDispatcher emailDispatcher;
    private final VerificationService verificationService;

    @Transactional
    public void sendVerificationEmail(VerificationMethod method, String jwt) {
        String accountId = jwtService.extractSubject(jwt);
        ZonedDateTime expiresAt = TimeConverter.toZonedDateTime.apply(jwtService.extractExpiresAt(jwt));
        Account account = accountService.findAccountById(accountId);

        String key = switch (method) {
            case LINK -> verificationService.generateVerificationLink(account, expiresAt);
            case CODE -> verificationService.generateVerificationCode(account, expiresAt);
        };

        try {
            emailDispatcher.sendVerificationEmail(method, account, key);
        } catch (MessagingException e) {
            throw new MailSendingException(e.getMessage(), e);
        }
    }

}
