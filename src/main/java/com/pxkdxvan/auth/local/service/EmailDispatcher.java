package com.pxkdxvan.auth.local.service;

import com.pxkdxvan.auth.local.model.VerificationMethod;
import com.pxkdxvan.auth.shared.model.Account;
import com.pxkdxvan.auth.local.properties.MailProperties;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class EmailDispatcher {

    private static final String DEFAULT_SUBJECT = "Confirm your email address";

    private final String DEFAULT_ENCODING;
    private final String SENDER_EMAIL;

    private final TemplateService templateService;
    private final JavaMailSender mailSender;

    EmailDispatcher(MailProperties mailProperties, TemplateService templateService, JavaMailSender mailSender) {
        DEFAULT_ENCODING = mailProperties.defaultEncoding();
        SENDER_EMAIL = mailProperties.username();
        this.templateService = templateService;
        this.mailSender = mailSender;
    }

    private void sendEmail(Account account, String subject, String htmlContent) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, DEFAULT_ENCODING);

        helper.setTo(account.getEmail());
        helper.setSubject(subject);
        helper.setFrom(SENDER_EMAIL);
        helper.setText(htmlContent, true);

        mailSender.send(message);
    }

    @Async
    public void sendVerificationEmail(VerificationMethod method, Account account, String key) throws MessagingException {
        String htmlContent = templateService.generateVerificationEmailContent(method, account, key);
        sendEmail(account, DEFAULT_SUBJECT, htmlContent);
    }

}
