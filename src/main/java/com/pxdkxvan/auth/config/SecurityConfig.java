package com.pxdkxvan.auth.config;

import com.pxdkxvan.auth.adapter.OAuth2LoginAdapter;

import com.pxdkxvan.auth.adapter.OidcLoginAdapter;
import com.pxdkxvan.auth.filter.CorrelationFilter;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
class SecurityConfig {

    private final OAuth2LoginAdapter oauth2LoginAdapter;
    private final OidcLoginAdapter oidcLoginAdapter;

    private final CorrelationFilter correlationFilter;

    private final AuthenticationProvider authenticationProvider;

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .cors(Customizer.withDefaults())
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorize ->
                        authorize
                                .requestMatchers("/auth/email/send/**", "/auth/email/verify/code").authenticated()
                                .requestMatchers("/auth/**", "/login/oauth2/**").permitAll()
                                .anyRequest().authenticated())
                .sessionManagement(sessionManagement ->
                        sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .oauth2Login(oauth2 ->
                        oauth2
                                .userInfoEndpoint(infoEndpointConfig ->
                                        infoEndpointConfig
                                                .userService(oauth2LoginAdapter)
                                                .oidcUserService(oidcLoginAdapter))
                                .successHandler((req, resp, auth) ->
                                        req.getRequestDispatcher(req.getContextPath() + "/oauth2/login/callback").forward(req, resp)))
                .oauth2ResourceServer(oauth2 ->
                        oauth2.jwt(Customizer.withDefaults()))
                .addFilterBefore(correlationFilter, AbstractPreAuthenticatedProcessingFilter.class)
                .authenticationProvider(authenticationProvider)
                .build();
    }

}
