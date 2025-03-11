package com.pxkdxvan.auth.local.cleaner;

import com.pxkdxvan.auth.local.properties.CleanerProperties;
import com.pxkdxvan.auth.local.utils.TimeConverter;
import com.pxkdxvan.auth.shared.repository.AccountRepository;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.ZonedDateTime;

@Service
public class AccountCleaner {

    private final Duration ACCOUNT_VERIFICATION_GRACE_PERIOD;

    private final AccountRepository accountRepository;

    AccountCleaner(CleanerProperties cleanerProperties, AccountRepository accountRepository) {
        ACCOUNT_VERIFICATION_GRACE_PERIOD = cleanerProperties.account().verificationGracePeriod();
        this.accountRepository = accountRepository;
    }

    @Transactional
    @Scheduled(cron = "#{@'app.cleaner-com.pxkdxvan.auth.local.properties.CleanerProperties'.account().cron()}")
    int cleanUnverifiedAccounts() {
        ZonedDateTime relativeVerifying = TimeConverter.back.apply(ZonedDateTime.now(), ACCOUNT_VERIFICATION_GRACE_PERIOD);
        return accountRepository.deleteByVerifiedAndCreatedAtBefore(false, relativeVerifying);
    }

}
