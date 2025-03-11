package com.pxkdxvan.auth.shared.repository;

import com.pxkdxvan.auth.shared.model.Account;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.ZonedDateTime;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AccountRepository extends JpaRepository<Account, UUID> {
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    Optional<Account> findByEmail(String email);
    int deleteByVerifiedAndCreatedAtBefore(Boolean verified, ZonedDateTime createdAt);
}