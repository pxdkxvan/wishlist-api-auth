package com.pxdkxvan.auth.mapper;

import com.pxdkxvan.auth.model.Role;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class RoleMapper {

    private static final String ROLE_PREFIX = "ROLE_";

    public static GrantedAuthority toGrantedAuthority(Role role) {
        return new SimpleGrantedAuthority(ROLE_PREFIX + role.getName());
    }

    public static List<GrantedAuthority> toGrantedAuthorityList(Collection<Role> roles) {
        return roles
                .stream()
                .map(RoleMapper::toGrantedAuthority)
                .toList();
    }

}
