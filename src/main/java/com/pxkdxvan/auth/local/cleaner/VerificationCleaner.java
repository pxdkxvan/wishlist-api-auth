package com.pxkdxvan.auth.local.cleaner;

import com.pxkdxvan.auth.local.repository.VerificationRepository;

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
    @Scheduled(cron = "#{@'app.cleaner-com.pxkdxvan.auth.local.properties.CleanerProperties'.verification().cron()}")
    int cleanExpiredVerifications() {
        return verificationRepository.deleteByExpiresAtBefore(ZonedDateTime.now());
    }

}
