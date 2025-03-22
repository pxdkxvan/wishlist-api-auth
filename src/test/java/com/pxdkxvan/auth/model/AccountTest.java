package com.pxdkxvan.auth.model;

import com.pxdkxvan.auth.support.factory.TestEntityFactory;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;

@Import(TestEntityFactory.class)
@ExtendWith(SpringExtension.class)
@RequiredArgsConstructor(onConstructor_ = @Autowired, access = AccessLevel.PACKAGE)
public class AccountTest {

    private final TestEntityFactory testEntityFactory;

    @Test
    void bindAuthorizationSuccess() {
        Authorization authorization = testEntityFactory.createAuthorization(Provider.LOCAL);
        Account account = authorization.getAccount();

        account.bindAuthorization(authorization);
        assertThat(account.getAuthorizations()).hasSize(1);
    }

    @Test
    void bindVerificationSuccess() {
        Verification verification = testEntityFactory.createVerification(VerificationMethod.LINK);
        Account account = verification.getAccount();

        account.bindVerification(verification);
        assertThat(account.getVerifications()).hasSize(1);

        account.unbindVerification(verification);
        assertThat(account.getVerifications()).isEmpty();
    }

}
