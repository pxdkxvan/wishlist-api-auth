package com.pxdkxvan.auth.service;

import com.pxdkxvan.auth.config.ConstantsConfig;
import com.pxdkxvan.auth.exception.verification.VerificationKeyExpiredException;
import com.pxdkxvan.auth.exception.verification.VerificationNotFoundException;
import com.pxdkxvan.auth.factory.VerificationFactory;
import com.pxdkxvan.auth.util.TimeUtils;
import com.pxdkxvan.auth.model.Account;
import com.pxdkxvan.auth.model.Verification;
import com.pxdkxvan.auth.model.VerificationMethod;
import com.pxdkxvan.auth.properties.VerificationProperties;
import com.pxdkxvan.auth.repository.VerificationRepository;
import com.pxdkxvan.auth.generator.VerificationGenerator;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.UUID;

@Service
public class VerificationService {

    private static final String VERIFY_LINK_BASEURL = "http://localhost:8080";
    private static final String VERIFY_LINK_CONTEXT_PATH = "/auth/email/verify";
    private static final String VERIFY_LINK_URL = "/link";

    private final Duration VERIFICATION_CODE_DURATION;

    private final AccountService accountService;
    private final VerificationFactory verificationFactory;
    private final VerificationRepository verificationRepository;
    private final VerificationGenerator verificationGenerator;

    VerificationService(VerificationProperties verificationProperties, AccountService accountService, VerificationFactory verificationFactory,
                        VerificationRepository verificationRepository, VerificationGenerator verificationGenerator) {
        VERIFICATION_CODE_DURATION = verificationProperties.key().code().duration();
        this.accountService = accountService;
        this.verificationFactory = verificationFactory;
        this.verificationRepository = verificationRepository;
        this.verificationGenerator = verificationGenerator;
    }

    String generateVerificationKey(VerificationMethod method, Account account, ZonedDateTime expiresAt) {
        String key = verificationGenerator.random(method);

        Verification verification = verificationRepository
                .findByMethodAndAccount(method, account)
                .orElseGet(() -> verificationFactory.create(key, method, account, expiresAt));

        verification.setKey(key);
        verification.setExpiresAt(expiresAt);

        verification = verificationRepository.save(verification);
        account.bindVerification(verification);

        return key;
    }

    @Transactional
    public String generateVerificationLink(Account account, ZonedDateTime expiresAt) {
        String token = generateVerificationKey(VerificationMethod.LINK, account, expiresAt);
        return UriComponentsBuilder
                .fromPath(VERIFY_LINK_BASEURL)
                .path(VERIFY_LINK_CONTEXT_PATH)
                .path(VERIFY_LINK_URL)
                .queryParam(ConstantsConfig.VERIFICATION_LINK_PARAM_NAME, token)
                .build()
                .toUriString();
    }

    @Transactional
    public String generateVerificationCode(Account account, ZonedDateTime expiresAt) {
        ZonedDateTime limit = TimeUtils.ahead.apply(ZonedDateTime.now(), VERIFICATION_CODE_DURATION);
        if (limit.isBefore(expiresAt)) expiresAt = limit;
        return generateVerificationKey(VerificationMethod.CODE, account, expiresAt);
    }

    void verifyAccount(Verification verification) {
        ZonedDateTime expiresAt = verification.getExpiresAt();
        if (ZonedDateTime.now().isAfter(expiresAt)) throw new VerificationKeyExpiredException();

        Account account = verification.getAccount();
        accountService.verifyAccount(account);

        account.unbindVerification(verification);
        verificationRepository.delete(verification);
    }

    @Transactional
    public void verifyAccountWithToken(String token) {
        verifyAccount(verificationRepository
                .findByKey(token)
                .orElseThrow(VerificationNotFoundException::new));
    }

    @Transactional
    public void verifyAccountWithCode(String code, UUID accountId) {
        verifyAccount(verificationRepository
                .findByKeyAndAccountId(code, accountId)
                .orElseThrow(VerificationNotFoundException::new));
    }

}
