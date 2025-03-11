package com.pxkdxvan.auth.local.repository;

import com.pxkdxvan.auth.local.model.Verification;

import com.pxkdxvan.auth.local.model.VerificationMethod;
import com.pxkdxvan.auth.shared.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.ZonedDateTime;
import java.util.Optional;

@Repository
public interface VerificationRepository extends JpaRepository<Verification, Long> {
    Optional<Verification> findByKey(String key);
    Optional<Verification> findByAccountAndMethod(Account account, VerificationMethod method);
    int deleteByExpiresAtBefore(ZonedDateTime expiresAt);
}
