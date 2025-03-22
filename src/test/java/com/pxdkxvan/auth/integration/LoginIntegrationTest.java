package com.pxdkxvan.auth.integration;

import com.pxdkxvan.auth.dto.LoginRequestDTO;
import com.pxdkxvan.auth.dto.ResponseDTO;
import com.pxdkxvan.auth.dto.TokenResponseDTO;
import com.pxdkxvan.auth.factory.RequestEntityFactory;
import com.pxdkxvan.auth.model.Authorization;
import com.pxdkxvan.auth.model.Provider;
import com.pxdkxvan.auth.repository.AccountRepository;
import com.pxdkxvan.auth.repository.AuthorizationRepository;
import com.pxdkxvan.auth.repository.RoleRepository;
import com.pxdkxvan.auth.response.ResponseCode;
import com.pxdkxvan.auth.support.config.Containers;
import com.pxdkxvan.auth.support.config.DotenvApplicationContextInitializer;
import com.pxdkxvan.auth.support.factory.TestEntityFactory;
import com.pxdkxvan.auth.support.factory.TestRequestDTOFactory;
import com.pxdkxvan.auth.support.util.ResponseVerifier;
import com.pxdkxvan.auth.support.util.TestRandomizer;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.context.ImportTestcontainers;
import org.springframework.context.annotation.Import;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.repository.CrudRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.web.client.RestOperations;

import java.util.ArrayList;
import java.util.function.Consumer;
import java.util.stream.Stream;

import static com.pxdkxvan.auth.support.config.ApiConstants.LOGIN_ENDPOINT;
import static com.pxdkxvan.auth.support.config.TestConstants.ACCOUNT_EMAIL;
import static com.pxdkxvan.auth.support.config.TestConstants.ACCOUNT_USERNAME;

@ImportTestcontainers(Containers.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration(initializers = DotenvApplicationContextInitializer.class)
@RequiredArgsConstructor(onConstructor_ = @Autowired, access = AccessLevel.PACKAGE)
@Import({TestEntityFactory.class, TestRequestDTOFactory.class, TestRandomizer.class, ResponseVerifier.class})
class LoginIntegrationTest {

    private static final Provider PROVIDER = Provider.LOCAL;

    @Value("http://localhost:${local.server.port}/")
    private String BASE_URL;

    private final RestOperations restOperations;

    private final RoleRepository roleRepository;
    private final AccountRepository accountRepository;
    private final AuthorizationRepository authorizationRepository;

    private final TestEntityFactory testEntityFactory;
    private final TestRequestDTOFactory testRequestDTOFactory;
    private final RequestEntityFactory requestEntityFactory;

    private final TestRandomizer testRandomizer;
    private final ResponseVerifier responseVerifier;

    @BeforeEach
    void cleanUp() {
        Stream
                .of(authorizationRepository, accountRepository, roleRepository)
                .forEach(CrudRepository::deleteAll);
    }

    @ParameterizedTest
    @ValueSource(strings = {ACCOUNT_USERNAME, ACCOUNT_EMAIL})
    void loginSuccess(String login) {
        Authorization testAuthorization = testEntityFactory.createAuthorization(PROVIDER);
        testAuthorization.getAccount().setVerified(true);
        authorizationRepository.save(testAuthorization);

        LoginRequestDTO testLoginRequestDTO = testRequestDTOFactory.createLoginRequestDTO(login);

        RequestEntity<LoginRequestDTO> testLoginRequest =
                requestEntityFactory.createPost(BASE_URL + LOGIN_ENDPOINT, testLoginRequestDTO);

        ResponseEntity<ResponseDTO<TokenResponseDTO>> testLoginResponse =
                restOperations.exchange(testLoginRequest, new ParameterizedTypeReference<>() {});

        responseVerifier.verifyNestedEntity(HttpStatus.OK, ResponseCode.ACCOUNT_LOGGED_IN, testLoginResponse);
    }

    @ParameterizedTest
    @NullAndEmptySource
    void loginFailedRequestNotValid(String value) {
        String login = testRandomizer.randomLogin();

        ArrayList<Consumer<LoginRequestDTO>> testFieldSetters = new ArrayList<>();
        testFieldSetters.add(dto -> dto.setLogin(value));
        testFieldSetters.add(dto -> dto.setPassword(value));

        for (Consumer<LoginRequestDTO> testFieldSetter : testFieldSetters) {
            LoginRequestDTO testLoginRequestDTO = testRequestDTOFactory.createLoginRequestDTO(login);
            testFieldSetter.accept(testLoginRequestDTO);

            RequestEntity<LoginRequestDTO> testLoginRequest =
                    requestEntityFactory.createPost(BASE_URL + LOGIN_ENDPOINT, testLoginRequestDTO);

            ResponseEntity<ResponseDTO<Void>> testLoginResponse =
                    restOperations.exchange(testLoginRequest, new ParameterizedTypeReference<>() {});

            responseVerifier.verifyPlainEntity(HttpStatus.BAD_REQUEST, ResponseCode.VALIDATION_FAILED, testLoginResponse);
        }
    }

    @Test
    void loginFailedLoginNotFound() {
        String login = testRandomizer.randomLogin();

        LoginRequestDTO testLoginRequestDTO = testRequestDTOFactory.createLoginRequestDTO(login);

        RequestEntity<LoginRequestDTO> testLoginRequest =
                requestEntityFactory.createPost(BASE_URL + LOGIN_ENDPOINT, testLoginRequestDTO);

        ResponseEntity<ResponseDTO<Void>> testLoginResponse =
                restOperations.exchange(testLoginRequest, new ParameterizedTypeReference<>() {});

        responseVerifier.verifyPlainEntity(HttpStatus.NOT_FOUND, ResponseCode.LOGIN_FAILED, testLoginResponse);
    }

    @Test
    void loginFailedPasswordMismatch() {
        String login = testRandomizer.randomLogin();

        Authorization testAuthorization = testEntityFactory.createAuthorization(PROVIDER);
        testAuthorization.getAccount().setVerified(true);
        authorizationRepository.save(testAuthorization);

        LoginRequestDTO testLoginRequestDTO = testRequestDTOFactory.createLoginRequestDTO(login);
        testLoginRequestDTO.setPassword(login);

        RequestEntity<LoginRequestDTO> testLoginRequest =
                requestEntityFactory.createPost(BASE_URL + LOGIN_ENDPOINT, testLoginRequestDTO);

        ResponseEntity<ResponseDTO<Void>> testLoginResponse =
                restOperations.exchange(testLoginRequest, new ParameterizedTypeReference<>() {});

        responseVerifier.verifyPlainEntity(HttpStatus.BAD_REQUEST, ResponseCode.LOGIN_FAILED, testLoginResponse);
    }

    @Test
    void loginFailedAccountNotVerified() {
        String login = testRandomizer.randomLogin();

        Authorization testAuthorization = testEntityFactory.createAuthorization(PROVIDER);
        authorizationRepository.save(testAuthorization);

        LoginRequestDTO testLoginRequestDTO = testRequestDTOFactory.createLoginRequestDTO(login);

        RequestEntity<LoginRequestDTO> testLoginRequest =
                requestEntityFactory.createPost(BASE_URL + LOGIN_ENDPOINT, testLoginRequestDTO);

        ResponseEntity<ResponseDTO<Void>> testLoginResponse =
                restOperations.exchange(testLoginRequest, new ParameterizedTypeReference<>() {});

        responseVerifier.verifyPlainEntity(HttpStatus.UNAUTHORIZED, ResponseCode.ACCOUNT_NOT_VERIFIED, testLoginResponse);
    }

}
