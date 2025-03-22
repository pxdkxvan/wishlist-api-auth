package com.pxdkxvan.auth.cleaner;

import com.pxdkxvan.auth.properties.CleanerProperties;
import com.pxdkxvan.auth.util.TimeUtils;
import com.pxdkxvan.auth.repository.AccountRepository;

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
    @Scheduled(cron = "#{@'app.cleaner-com.pxdkxvan.auth.properties.CleanerProperties'.account().cron()}")
    public int cleanUnverifiedAccounts() {
        ZonedDateTime relativeVerifying = TimeUtils.back.apply(ZonedDateTime.now(), ACCOUNT_VERIFICATION_GRACE_PERIOD);
        return accountRepository.deleteByVerifiedIsFalseAndCreatedAtBefore(relativeVerifying);
    }

}
