package com.pxdkxvan.auth.adapter;

import com.pxdkxvan.auth.model.VerificationMethod;
import com.pxdkxvan.auth.model.Account;
import com.pxdkxvan.auth.properties.MailProperties;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class SMTPAdapter {

    private static final String DEFAULT_SUBJECT = "Confirm your email address";

    private final String DEFAULT_ENCODING;
    private final String SENDER_EMAIL;

    private final ThymeleafAdapter thymeleafAdapter;
    private final JavaMailSender mailSender;

    SMTPAdapter(MailProperties mailProperties, ThymeleafAdapter thymeleafAdapter, JavaMailSender mailSender) {
        DEFAULT_ENCODING = mailProperties.defaultEncoding();
        SENDER_EMAIL = mailProperties.username();
        this.thymeleafAdapter = thymeleafAdapter;
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
        String htmlContent = thymeleafAdapter.generateVerificationEmailContent(method, account, key);
        sendEmail(account, DEFAULT_SUBJECT, htmlContent);
    }

}
