package com.pxdkxvan.auth.support.factory;

import com.pxdkxvan.auth.dto.CodeRequestDTO;
import com.pxdkxvan.auth.dto.GitHubEmailResponseDTO;
import com.pxdkxvan.auth.dto.LoginRequestDTO;
import com.pxdkxvan.auth.dto.RegisterRequestDTO;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import org.springframework.boot.test.context.TestComponent;

import static com.pxdkxvan.auth.support.config.TestConstants.*;

@TestComponent
@NoArgsConstructor(access = AccessLevel.PACKAGE)
public final class TestRequestDTOFactory {

    public LoginRequestDTO createLoginRequestDTO(String login) {
        return LoginRequestDTO
                .builder()
                .login(login)
                .password(ACCOUNT_PASSWORD)
                .build();
    }

    public RegisterRequestDTO createRegisterRequestDTO() {
        return RegisterRequestDTO
                .builder()
                .username(ACCOUNT_USERNAME)
                .email(ACCOUNT_EMAIL)
                .password(ACCOUNT_PASSWORD)
                .build();
    }

    public CodeRequestDTO createCodeRequestDTO() {
        return CodeRequestDTO
                .builder()
                .code(VERIFICATION_CODE)
                .build();
    }

    public GitHubEmailResponseDTO createGitHubEmailResponseDTO() {
        return GitHubEmailResponseDTO
                .builder()
                .email(ACCOUNT_EMAIL)
                .primary(true)
                .build();
    }

}
