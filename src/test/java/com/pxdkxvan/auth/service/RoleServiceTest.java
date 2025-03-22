package com.pxdkxvan.auth.service;

import com.pxdkxvan.auth.support.factory.TestEntityFactory;
import com.pxdkxvan.auth.exception.auth.RoleNotFoundException;
import com.pxdkxvan.auth.model.Role;
import com.pxdkxvan.auth.repository.RoleRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.util.Optional;

import static com.pxdkxvan.auth.config.ConstantsConfig.DEFAULT_ROLE_NAME;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringJUnitConfig(classes = {RoleService.class, TestEntityFactory.class})
@RequiredArgsConstructor(onConstructor_ = @Autowired, access = AccessLevel.PACKAGE)
class RoleServiceTest {

    @MockitoBean
    private final RoleRepository roleRepository;
    private final RoleService roleService;
    private final TestEntityFactory testEntityFactory;

    @Test
    void getRoleSuccess() {
        Role testRole = testEntityFactory.createRole();
        when(roleRepository.findByName(DEFAULT_ROLE_NAME)).thenReturn(Optional.of(testRole));
        assertThat(roleService.getDefaultRole()).isEqualTo(testRole);
        verify(roleRepository).findByName(DEFAULT_ROLE_NAME);
    }

    @Test
    void getRoleFailedRoleNotFound() {
        when(roleRepository.findByName(DEFAULT_ROLE_NAME)).thenReturn(Optional.empty());

        assertThatThrownBy(roleService::getDefaultRole)
                .isInstanceOf(RoleNotFoundException.class)
                .hasMessageContaining(DEFAULT_ROLE_NAME);

        verify(roleRepository).findByName(DEFAULT_ROLE_NAME);
    }

}
