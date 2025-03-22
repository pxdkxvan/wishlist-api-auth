package com.pxdkxvan.auth.service;

import com.pxdkxvan.auth.support.factory.TestEntityFactory;
import com.pxdkxvan.auth.adapter.SMTPAdapter;
import com.pxdkxvan.auth.exception.mail.MailSendingException;
import com.pxdkxvan.auth.model.Account;
import com.pxdkxvan.auth.model.Verification;
import com.pxdkxvan.auth.model.VerificationMethod;
import com.pxdkxvan.auth.support.util.TestRandomizer;

import jakarta.mail.MessagingException;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.time.ZonedDateTime;
import java.util.UUID;

import static com.pxdkxvan.auth.support.config.TestConstants.*;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import static org.mockito.Mockito.*;

@RequiredArgsConstructor(onConstructor_ = @Autowired, access = AccessLevel.PACKAGE)
@SpringJUnitConfig(classes = {MailingService.class, TestEntityFactory.class, TestRandomizer.class})
class MailingServiceTest {

    @MockitoBean
    private final SMTPAdapter smtpAdapter;

    @MockitoBean
    private final JwtService jwtService;
    @MockitoBean
    private final AccountService accountService;
    @MockitoBean
    private final VerificationService verificationService;
    private final MailingService mailingService;

    private final TestEntityFactory testEntityFactory;

    private final TestRandomizer testRandomizer;

    @ParameterizedTest
    @EnumSource(VerificationMethod.class)
    void sendVerificationEmailSuccess(VerificationMethod method) throws MessagingException {
        Verification testVerification = testEntityFactory.createIdentifiedVerification(method);
        Account testAccount = testVerification.getAccount();
        UUID testAccountId = testAccount.getId();
        String testKey = testVerification.getKey();

        when(jwtService.extractSubject(JWT_VALUE)).thenReturn(String.valueOf(testAccountId));
        when(jwtService.extractExpiresAt(JWT_VALUE)).thenReturn(JWT_EXPIRES_AT);

        when(accountService.findAccountById(any(UUID.class))).thenReturn(testAccount);

        when(verificationService.generateVerificationLink(eq(testAccount), any(ZonedDateTime.class))).thenReturn(VERIFICATION_TOKEN);
        when(verificationService.generateVerificationCode(eq(testAccount), any(ZonedDateTime.class))).thenReturn(VERIFICATION_CODE);

        mailingService.sendVerificationEmail(method, JWT_VALUE);

        verify(jwtService).extractSubject(JWT_VALUE);
        verify(jwtService).extractExpiresAt(JWT_VALUE);

        verify(accountService).findAccountById(any(UUID.class));

        switch (method) {
            case LINK -> verify(verificationService).generateVerificationLink(eq(testAccount), any(ZonedDateTime.class));
            case CODE -> verify(verificationService).generateVerificationCode(eq(testAccount), any(ZonedDateTime.class));
        }

        verify(smtpAdapter).sendVerificationEmail(method, testAccount, testKey);
    }

    @Test
    void sendVerificationEmailFailedMessagingException() throws MessagingException {
        VerificationMethod method = testRandomizer.randomVerificationMethod();

        Verification testVerification = testEntityFactory.createIdentifiedVerification(method);
        Account testAccount = testVerification.getAccount();
        UUID testAccountId = testAccount.getId();
        String testKey = testVerification.getKey();

        when(jwtService.extractSubject(JWT_VALUE)).thenReturn(String.valueOf(testAccountId));
        when(jwtService.extractExpiresAt(JWT_VALUE)).thenReturn(JWT_EXPIRES_AT);

        when(accountService.findAccountById(any(UUID.class))).thenReturn(testAccount);

        when(verificationService.generateVerificationLink(eq(testAccount), any(ZonedDateTime.class))).thenReturn(VERIFICATION_TOKEN);
        when(verificationService.generateVerificationCode(eq(testAccount), any(ZonedDateTime.class))).thenReturn(VERIFICATION_CODE);

        doThrow(new MessagingException())
                .when(smtpAdapter)
                .sendVerificationEmail(method, testAccount, testKey);

        assertThatThrownBy(() -> mailingService.sendVerificationEmail(method, JWT_VALUE))
                .isInstanceOf(MailSendingException.class);

        verify(jwtService).extractSubject(JWT_VALUE);
        verify(jwtService).extractExpiresAt(JWT_VALUE);

        verify(accountService).findAccountById(any(UUID.class));

        switch (method) {
            case LINK -> verify(verificationService).generateVerificationLink(eq(testAccount), any(ZonedDateTime.class));
            case CODE -> verify(verificationService).generateVerificationCode(eq(testAccount), any(ZonedDateTime.class));
        }

        verify(smtpAdapter).sendVerificationEmail(method, testAccount, testKey);
    }

}
