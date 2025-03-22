package com.pxdkxvan.auth.config;

import com.pxdkxvan.auth.exception.auth.LoginNotFoundException;
import com.pxdkxvan.auth.util.EmailMasker;
import com.pxdkxvan.auth.mapper.AccountMapper;
import com.pxdkxvan.auth.model.Authorization;
import com.pxdkxvan.auth.model.Provider;
import com.pxdkxvan.auth.repository.AuthorizationRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;

@Configuration
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
class ApplicationConfig {

    private final AuthorizationRepository authorizationRepository;
    private final AccountMapper accountMapper;

    private Authorization getLocalCredentialsByLogin(String login) {
        return authorizationRepository
                .findByLoginAndProvider(login, Provider.LOCAL)
                .orElseThrow(() -> new LoginNotFoundException(EmailMasker.mask(login)));
    }

    @Bean
    UserDetailsService userDetailsService() {
        return login -> accountMapper.toAccountDetails(getLocalCredentialsByLogin(login));
    }

    @Bean
    AuthenticationProvider authenticationProvider(AccountConfig accountConfig) {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService());
        provider.setPasswordEncoder(accountConfig.passwordEncoder());
        provider.setHideUserNotFoundExceptions(false);
        return provider;
    }

    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

}
