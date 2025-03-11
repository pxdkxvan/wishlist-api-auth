package com.pxkdxvan.auth.shared.security;

import com.pxkdxvan.auth.shared.mapper.RoleMapper;
import com.pxkdxvan.auth.shared.model.Role;

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
