package com.pxdkxvan.auth.factory;

import com.pxdkxvan.auth.model.Account;
import com.pxdkxvan.auth.model.Role;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@NoArgsConstructor(access = AccessLevel.PACKAGE)
public final class AccountFactory {
    public Account create(String username, String email, Set<Role> roles) {
        return Account
                .builder()
                .username(username)
                .email(email)
                .roles(roles)
                .build();
    }
}
