package com.pxdkxvan.auth.repository;

import com.pxdkxvan.auth.model.Verification;

import com.pxdkxvan.auth.model.VerificationMethod;
import com.pxdkxvan.auth.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.ZonedDateTime;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface VerificationRepository extends JpaRepository<Verification, Long> {
    Optional<Verification> findByKey(String key);
    Optional<Verification> findByKeyAndAccountId(String key, UUID accountId);
    Optional<Verification> findByMethodAndAccount(VerificationMethod method, Account account);
    int deleteByExpiresAtBefore(ZonedDateTime expiresAt);
}
