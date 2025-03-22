package com.pxdkxvan.auth.integration;

import com.pxdkxvan.auth.config.ConstantsConfig;
import com.pxdkxvan.auth.dto.ResponseDTO;
import com.pxdkxvan.auth.dto.TokenResponseDTO;
import com.pxdkxvan.auth.factory.CookieFactory;
import com.pxdkxvan.auth.factory.RequestEntityFactory;
import com.pxdkxvan.auth.model.Account;
import com.pxdkxvan.auth.repository.AccountRepository;
import com.pxdkxvan.auth.response.ResponseCode;
import com.pxdkxvan.auth.security.JwtType;
import com.pxdkxvan.auth.service.JwtService;
import com.pxdkxvan.auth.support.config.Containers;
import com.pxdkxvan.auth.support.config.DotenvApplicationContextInitializer;
import com.pxdkxvan.auth.support.factory.TestEntityFactory;
import com.pxdkxvan.auth.support.util.ResponseVerifier;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.context.ImportTestcontainers;
import org.springframework.context.annotation.Import;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.web.client.RestOperations;

import java.util.UUID;

import static com.pxdkxvan.auth.support.config.ApiConstants.REFRESH_ENDPOINT;

@ImportTestcontainers(Containers.class)
@Import({TestEntityFactory.class, ResponseVerifier.class})
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration(initializers = DotenvApplicationContextInitializer.class)
@RequiredArgsConstructor(onConstructor_ = @Autowired, access = AccessLevel.PACKAGE)
class RefreshIntegrationTest {

    @Value("http://localhost:${local.server.port}/")
    private String BASE_URL;

    private final RestOperations restOperations;

    private final JwtService jwtService;

    private final AccountRepository accountRepository;

    private final CookieFactory cookieFactory;
    private final TestEntityFactory testEntityFactory;
    private final RequestEntityFactory requestEntityFactory;

    private final ResponseVerifier responseVerifier;

    @Test
    void refreshSuccess() {
        Account account = testEntityFactory.createAccount();
        UUID testAccountId = accountRepository.save(account).getId();
        String testJwt = jwtService.generateToken(JwtType.TEMPORARY, String.valueOf(testAccountId));

        ResponseCookie testRefreshRequestCookie = cookieFactory.createDefault(ConstantsConfig.REFRESH_COOKIE_NAME, testJwt);

        RequestEntity<Void> testRefreshRequest =
                requestEntityFactory.createPost(BASE_URL + REFRESH_ENDPOINT, testRefreshRequestCookie, null);

        ResponseEntity<ResponseDTO<TokenResponseDTO>> testRefreshResponse =
                restOperations.exchange(testRefreshRequest, new ParameterizedTypeReference<>() {});

        responseVerifier.verifyNestedEntity(HttpStatus.OK, ResponseCode.TOKEN_REFRESHED, testRefreshResponse);
    }

    @Test
    void refreshFailedCookieNotFound() {
        RequestEntity<Void> testRefreshRequest =
                requestEntityFactory.createPost(BASE_URL + REFRESH_ENDPOINT, null);

        ResponseEntity<ResponseDTO<Void>> testRefreshResponse =
                restOperations.exchange(testRefreshRequest, new ParameterizedTypeReference<>() {});

        responseVerifier.verifyPlainEntity(HttpStatus.UNAUTHORIZED, ResponseCode.MISSING_REFRESH_TOKEN, testRefreshResponse);
    }

}
