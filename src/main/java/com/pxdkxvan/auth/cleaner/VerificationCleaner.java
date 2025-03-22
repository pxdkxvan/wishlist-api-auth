package com.pxdkxvan.auth.cleaner;

import com.pxdkxvan.auth.repository.VerificationRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;

@Service
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public class VerificationCleaner {

    private final VerificationRepository verificationRepository;

    @Transactional
    @Scheduled(cron = "#{@'app.cleaner-com.pxdkxvan.auth.properties.CleanerProperties'.verification().cron()}")
    public int cleanExpiredVerifications() {
        return verificationRepository.deleteByExpiresAtBefore(ZonedDateTime.now());
    }

}
