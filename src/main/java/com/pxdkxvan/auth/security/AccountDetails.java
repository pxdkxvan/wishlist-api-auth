package com.pxdkxvan.auth.security;

import com.pxdkxvan.auth.mapper.RoleMapper;
import com.pxdkxvan.auth.model.Role;

import lombok.*;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public final class AccountDetails implements UserDetails {

    private UUID id;
    private String username;
    private String email;
    private Boolean verified;
    private String password;
    private Set<Role> roles;

    @Override
    public boolean isEnabled() {
        return verified;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return RoleMapper.toGrantedAuthorityList(roles);
    }

}
