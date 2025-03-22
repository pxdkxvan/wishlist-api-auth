package com.pxdkxvan.auth.repository;

import com.pxdkxvan.auth.support.factory.TestEntityFactory;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import static com.pxdkxvan.auth.support.config.TestConstants.ROLE_NAME;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(TestEntityFactory.class)
@RequiredArgsConstructor(onConstructor_ = @Autowired, access = AccessLevel.PACKAGE)
class RoleRepositoryTest {

    private final RoleRepository roleRepository;
    private final TestEntityFactory testEntityFactory;

    @Test
    void saveAndFindRoleSuccess() {
        roleRepository.save(testEntityFactory.createRole());
        assertThat(roleRepository.findByName(ROLE_NAME))
                .isPresent()
                .hasValueSatisfying(role -> assertThat(role.getName()).isEqualTo(ROLE_NAME));
    }

    @Test
    void findRoleFailedRoleNotFound() {
        assertThat(roleRepository.findByName(ROLE_NAME)).isNotPresent();
    }

}
