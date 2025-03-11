package com.pxkdxvan.auth.shared.config;

import com.pxkdxvan.auth.local.exception.auth.LoginNotFoundException;
import com.pxkdxvan.auth.local.utils.EmailMasker;
import com.pxkdxvan.auth.shared.mapper.AccountMapper;
import com.pxkdxvan.auth.shared.model.Authorization;

import com.pxkdxvan.auth.shared.model.Provider;
import com.pxkdxvan.auth.shared.repository.AuthorizationRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
class ApplicationConfig {

    private final AuthorizationRepository authorizationRepository;

    private Authorization getLocalCredentialsByLogin(String login) {
        return authorizationRepository
                .findByLoginAndProvider(login, Provider.LOCAL)
                .orElseThrow(() -> new LoginNotFoundException(EmailMasker.mask(login)));
    }

    @Bean
    UserDetailsService userDetailsService() {
        return login -> AccountMapper.toAccountDetails(getLocalCredentialsByLogin(login));
    }

    @Bean
    AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService());
        provider.setPasswordEncoder(passwordEncoder());
        provider.setHideUserNotFoundExceptions(false);
        return provider;
    }

    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
