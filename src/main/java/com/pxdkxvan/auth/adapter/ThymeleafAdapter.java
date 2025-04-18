package com.pxdkxvan.auth.adapter;

import com.pxdkxvan.auth.model.VerificationMethod;
import com.pxdkxvan.auth.config.ConstantsConfig;
import com.pxdkxvan.auth.model.Account;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public class ThymeleafAdapter {

    private static final String EMAIL_VERIFY_LINK_TEMPLATE_NAME = "verification-link";
    private static final String EMAIL_VERIFY_CODE_TEMPLATE_NAME = "verification-code";

    private final TemplateEngine templateEngine;

    public String generateVerificationEmailContent(VerificationMethod method, Account account, String key) {
        Context context = new Context();

        context.setVariable("appName", ConstantsConfig.APP_NAME);
        context.setVariable("username", account.getUsername());
        context.setVariable("email", account.getEmail());
        context.setVariable("key", key);

        String templateName = switch (method) {
            case LINK -> EMAIL_VERIFY_LINK_TEMPLATE_NAME;
            case CODE -> EMAIL_VERIFY_CODE_TEMPLATE_NAME;
        };

        return templateEngine.process(templateName, context);
    }

}
