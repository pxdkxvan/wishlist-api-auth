package com.pxdkxvan.auth.support.factory;

import com.pxdkxvan.auth.model.Role;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import org.springframework.boot.test.context.TestComponent;

@TestComponent
@NoArgsConstructor(access = AccessLevel.PACKAGE)
public final class RoleFactory {
    public Role create(String name) {
        return Role
                .builder()
                .name(name)
                .build();
    }
}
