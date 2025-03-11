package com.pxkdxvan.auth.shared.factory;

import com.pxkdxvan.auth.shared.model.Account;
import com.pxkdxvan.auth.shared.model.Role;

import org.springframework.stereotype.Component;

import java.util.Set;

@Component
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
