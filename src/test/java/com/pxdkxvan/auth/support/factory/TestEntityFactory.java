package com.pxdkxvan.auth.support.factory;

import com.pxdkxvan.auth.config.AccountConfig;
import com.pxdkxvan.auth.factory.*;
import com.pxdkxvan.auth.model.*;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

import org.springframework.boot.test.context.TestComponent;
import org.springframework.context.annotation.Import;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

import static com.pxdkxvan.auth.support.config.TestConstants.*;

@TestComponent
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
@Import({AccountConfig.class, RoleFactory.class, AccountFactory.class,
        AuthorizationFactory.class, VerificationFactory.class, OAuth2DataFactory.class})
public final class TestEntityFactory {

    private final RoleFactory roleFactory;
    private final AccountFactory accountFactory;
    private final AuthorizationFactory authorizationFactory;
    private final VerificationFactory verificationFactory;
    private final OAuth2DataFactory oAuth2DataFactory;

    public Role createRole() {
        return roleFactory.create(ROLE_NAME);
    }

    public Account createAccount() {
        Set<Role> testRoles = new HashSet<>(Set.of(createRole()));
        return accountFactory.create(ACCOUNT_USERNAME, ACCOUNT_EMAIL, testRoles);
    }

    public Account createIdentifiedAccount() {
        Account testAccount = createAccount();
        testAccount.setId(UUID.randomUUID());
        return testAccount;
    }

    public Authorization createAuthorization(Provider provider) {
        return (provider == Provider.LOCAL) ?
                authorizationFactory.createLocal(createAccount(), ACCOUNT_PASSWORD) :
                authorizationFactory.createOAuth2(createAccount(), provider, AUTHORIZATION_PROVIDER_ID);
    }

    public Authorization createIdentifiedAuthorization(Provider provider) {
        Authorization testAuthorization = createAuthorization(provider);
        testAuthorization.setId(ThreadLocalRandom.current().nextLong());
        testAuthorization.setAccount(createIdentifiedAccount());
        return testAuthorization;
    }

    public Verification createVerification(VerificationMethod method) {
        String testKey = (method == VerificationMethod.LINK) ? VERIFICATION_TOKEN : VERIFICATION_CODE;
        return verificationFactory.create(testKey, method, createAccount(), VERIFICATION_EXPIRES_AT);
    }

    public Verification createIdentifiedVerification(VerificationMethod method) {
        Verification testVerification = createVerification(method);
        testVerification.setId(ThreadLocalRandom.current().nextLong());
        testVerification.setAccount(createIdentifiedAccount());
        return testVerification;
    }

    public OAuth2AccountData createOAuth2AccountData() {
        return oAuth2DataFactory.create(ACCOUNT_USERNAME, ACCOUNT_EMAIL, AUTHORIZATION_PROVIDER_ID);
    }

}
