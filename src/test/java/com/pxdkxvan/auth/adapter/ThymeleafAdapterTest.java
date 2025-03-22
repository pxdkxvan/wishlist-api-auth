package com.pxdkxvan.auth.adapter;

import com.pxdkxvan.auth.support.factory.TestEntityFactory;
import com.pxdkxvan.auth.model.Account;
import com.pxdkxvan.auth.model.Verification;
import com.pxdkxvan.auth.model.VerificationMethod;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import static org.assertj.core.api.Assertions.assertThat;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringJUnitConfig(classes = {TestEntityFactory.class, ThymeleafAdapter.class})
@RequiredArgsConstructor(onConstructor_ = @Autowired, access = AccessLevel.PACKAGE)
class ThymeleafAdapterTest {

    @MockitoBean
    private final TemplateEngine templateEngine;
    private final ThymeleafAdapter thymeleafAdapter;
    private final TestEntityFactory testEntityFactory;

    @ParameterizedTest
    @EnumSource(VerificationMethod.class)
    void generateVerificationEmailContentSuccess(VerificationMethod method) {
        Verification verification = testEntityFactory.createVerification(method);
        Account account = verification.getAccount();
        String key = verification.getKey();

        when(templateEngine.process(any(String.class), any(Context.class))).thenReturn("");
        assertThat(thymeleafAdapter.generateVerificationEmailContent(method, account, key)).isEqualTo("");

        verify(templateEngine).process(any(String.class), any(Context.class));
    }

}
