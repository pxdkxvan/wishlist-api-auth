package com.pxdkxvan.auth.service;

import com.pxdkxvan.auth.config.ConstantsConfig;
import com.pxdkxvan.auth.exception.auth.RoleNotFoundException;
import com.pxdkxvan.auth.model.Role;
import com.pxdkxvan.auth.repository.RoleRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public class RoleService {

    private final RoleRepository roleRepository;

    private Role getRole(String name) {
        return roleRepository
                .findByName(name)
                .orElseThrow(() -> new RoleNotFoundException(name));
    }

    public Role getDefaultRole() {
        return getRole(ConstantsConfig.DEFAULT_ROLE_NAME);
    }

}
