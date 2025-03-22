package com.pxdkxvan.auth.service;

import com.pxdkxvan.auth.adapter.SMTPAdapter;
import com.pxdkxvan.auth.exception.mail.MailSendingException;
import com.pxdkxvan.auth.model.VerificationMethod;
import com.pxdkxvan.auth.util.TimeUtils;
import com.pxdkxvan.auth.model.Account;

import jakarta.mail.MessagingException;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public class MailingService {

    private final JwtService jwtService;
    private final AccountService accountService;
    private final VerificationService verificationService;

    private final SMTPAdapter SMTPAdapter;

    @Transactional
    public void sendVerificationEmail(VerificationMethod method, String jwt) {
        UUID accountId = UUID.fromString(jwtService.extractSubject(jwt));
        ZonedDateTime expiresAt = TimeUtils.toZonedDateTime.apply(jwtService.extractExpiresAt(jwt));
        Account account = accountService.findAccountById(accountId);

        String key = switch (method) {
            case LINK -> verificationService.generateVerificationLink(account, expiresAt);
            case CODE -> verificationService.generateVerificationCode(account, expiresAt);
        };

        try {
            SMTPAdapter.sendVerificationEmail(method, account, key);
        } catch (MessagingException e) {
            throw new MailSendingException(e.getMessage(), e);
        }
    }

}
