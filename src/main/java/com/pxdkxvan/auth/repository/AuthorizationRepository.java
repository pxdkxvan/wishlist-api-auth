package com.pxdkxvan.auth.repository;

import com.pxdkxvan.auth.model.Account;
import com.pxdkxvan.auth.model.Authorization;
import com.pxdkxvan.auth.model.Provider;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AuthorizationRepository extends JpaRepository<Authorization, Long> {
    @Query("SELECT auth " +
            "FROM Authorization auth " +
            "JOIN auth.account acc " +
            "WHERE (acc.username = :login OR acc.email = :login) AND auth.provider = :provider")
    Optional<Authorization> findByLoginAndProvider(String login, Provider provider);
    Optional<Authorization> findByAccountAndProvider(Account account, Provider provider);
}
