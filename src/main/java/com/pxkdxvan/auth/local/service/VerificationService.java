package com.pxkdxvan.auth.local.service;

import com.pxkdxvan.auth.local.exception.verification.VerificationKeyExpiredException;
import com.pxkdxvan.auth.local.exception.verification.VerificationNotFoundException;
import com.pxkdxvan.auth.local.factory.VerificationFactory;
import com.pxkdxvan.auth.local.utils.TimeConverter;
import com.pxkdxvan.auth.shared.model.Account;
import com.pxkdxvan.auth.local.model.Verification;
import com.pxkdxvan.auth.local.model.VerificationMethod;
import com.pxkdxvan.auth.shared.properties.VerificationProperties;
import com.pxkdxvan.auth.local.repository.VerificationRepository;
import com.pxkdxvan.auth.local.utils.VerificationGenerator;
import com.pxkdxvan.auth.shared.service.AccountService;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.Duration;
import java.time.ZonedDateTime;

@Service
public class VerificationService {

    private static final String VERIFY_LINK_BASEURL = "http://localhost:8080";
    private static final String VERIFY_LINK_CONTEXT_PATH = "/auth/email/verify";
    private static final String VERIFY_LINK_URL = "/link";
    private static final String VERIFY_LINK_PARAM_NAME = "token";

    private final Duration VERIFICATION_CODE_DURATION;

    private final AccountService accountService;

    private final VerificationFactory verificationFactory;

    private final VerificationRepository verificationRepository;

    VerificationService(VerificationProperties verificationProperties, AccountService accountService,
                               VerificationFactory verificationFactory, VerificationRepository verificationRepository) {
        VERIFICATION_CODE_DURATION = verificationProperties.key().code().duration();
        this.accountService = accountService;
        this.verificationFactory = verificationFactory;
        this.verificationRepository = verificationRepository;
    }

    protected String generateVerificationKey(VerificationMethod method, Account account, ZonedDateTime expiresAt) {
        String key = switch (method) {
            case LINK -> VerificationGenerator.token();
            case CODE -> VerificationGenerator.code();
        };

        Verification verification = verificationRepository
                .findByAccountAndMethod(account, method)
                .orElseGet(() -> verificationFactory.create(key, method, account, expiresAt));

        verification.setKey(key);
        verification.setExpiresAt(expiresAt);

        verification = verificationRepository.save(verification);
        account.bindVerification(verification);

        return key;
    }

    @Transactional
    public String generateVerificationLink(Account account, ZonedDateTime expiresAt) {
        return UriComponentsBuilder
                .fromPath(VERIFY_LINK_BASEURL)
                .path(VERIFY_LINK_CONTEXT_PATH)
                .path(VERIFY_LINK_URL)
                .queryParam(VERIFY_LINK_PARAM_NAME, generateVerificationKey(VerificationMethod.LINK, account, expiresAt))
                .build()
                .toUriString();
    }

    @Transactional
    public String generateVerificationCode(Account account, ZonedDateTime expiresAt) {
        ZonedDateTime limit = TimeConverter.ahead.apply(ZonedDateTime.now(), VERIFICATION_CODE_DURATION);
        if (limit.isBefore(expiresAt)) expiresAt = limit;
        return generateVerificationKey(VerificationMethod.CODE, account, expiresAt);
    }

    @Transactional
    public void verifyAccount(String key) {
        Verification verification = verificationRepository
                .findByKey(key)
                .orElseThrow(VerificationNotFoundException::new);

        ZonedDateTime expiresAt = verification.getExpiresAt();
        if (ZonedDateTime.now().isAfter(expiresAt)) throw new VerificationKeyExpiredException();

        Account account = verification.getAccount();
        accountService.verifyAccount(account);

        account.unbindVerification(verification);
        verificationRepository.delete(verification);
    }

}
