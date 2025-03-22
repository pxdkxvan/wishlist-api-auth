package com.pxdkxvan.auth.adapter;

import com.pxdkxvan.auth.support.factory.TestEntityFactory;
import com.pxdkxvan.auth.model.Account;
import com.pxdkxvan.auth.model.Verification;
import com.pxdkxvan.auth.model.VerificationMethod;
import com.pxdkxvan.auth.properties.MailProperties;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.ConfigDataApplicationContextInitializer;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@EnableConfigurationProperties(MailProperties.class)
@RequiredArgsConstructor(onConstructor_ = @Autowired, access = AccessLevel.PACKAGE)
@SpringJUnitConfig(classes = {TestEntityFactory.class, SMTPAdapter.class}, initializers = ConfigDataApplicationContextInitializer.class)
class SMTPAdapterTest {

    @MockitoBean
    private final ThymeleafAdapter thymeleafAdapter;
    @MockitoBean
    private final JavaMailSender mailSender;

    private final SMTPAdapter smtpAdapter;

    private final TestEntityFactory testEntityFactory;

    @ParameterizedTest
    @EnumSource(VerificationMethod.class)
    void sendVerificationEmailSuccess(VerificationMethod method) throws MessagingException {
        Verification verification = testEntityFactory.createVerification(method);
        Account account = verification.getAccount();
        String key = verification.getKey();

        when(thymeleafAdapter.generateVerificationEmailContent(method, account, key)).thenReturn("");

        MimeMessage message = new JavaMailSenderImpl().createMimeMessage();
        when(mailSender.createMimeMessage()).thenReturn(message);

        smtpAdapter.sendVerificationEmail(method, account, key);

        verify(thymeleafAdapter).generateVerificationEmailContent(method, account, key);
        verify(mailSender).createMimeMessage();
        verify(mailSender).send(message);
    }

}
