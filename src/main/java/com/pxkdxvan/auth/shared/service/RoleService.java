package com.pxkdxvan.auth.shared.service;

import com.pxkdxvan.auth.shared.exception.RoleNotFoundException;
import com.pxkdxvan.auth.shared.model.Role;
import com.pxkdxvan.auth.shared.repository.RoleRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public class RoleService {

    private static final String DEFAULT_ROLE = "MEMBER";

    private final RoleRepository roleRepository;

    private Role getRole(String name) {
        return roleRepository
                .findByName(name)
                .orElseThrow(() -> new RoleNotFoundException(name));
    }

    public Role getDefaultRole() {
        return getRole(DEFAULT_ROLE);
    }

}
