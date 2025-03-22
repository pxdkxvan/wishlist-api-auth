package com.pxdkxvan.auth.integration;

import com.pxdkxvan.auth.config.ConstantsConfig;
import com.pxdkxvan.auth.dto.CodeRequestDTO;
import com.pxdkxvan.auth.dto.RegisterRequestDTO;
import com.pxdkxvan.auth.dto.ResponseDTO;
import com.pxdkxvan.auth.dto.TokenResponseDTO;
import com.pxdkxvan.auth.factory.RequestEntityFactory;
import com.pxdkxvan.auth.generator.UsernameGenerator;
import com.pxdkxvan.auth.model.Account;
import com.pxdkxvan.auth.model.Role;
import com.pxdkxvan.auth.model.Verification;
import com.pxdkxvan.auth.model.VerificationMethod;
import com.pxdkxvan.auth.repository.AccountRepository;
import com.pxdkxvan.auth.repository.AuthorizationRepository;
import com.pxdkxvan.auth.repository.RoleRepository;
import com.pxdkxvan.auth.repository.VerificationRepository;
import com.pxdkxvan.auth.response.ResponseCode;
import com.pxdkxvan.auth.security.JwtType;
import com.pxdkxvan.auth.service.AccountService;
import com.pxdkxvan.auth.service.JwtService;
import com.pxdkxvan.auth.support.config.Containers;
import com.pxdkxvan.auth.support.config.DotenvApplicationContextInitializer;
import com.pxdkxvan.auth.support.factory.RoleFactory;
import com.pxdkxvan.auth.support.factory.TestEntityFactory;
import com.pxdkxvan.auth.support.factory.TestRequestDTOFactory;
import com.pxdkxvan.auth.support.util.ResponseVerifier;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.MethodSource;
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
import org.springframework.web.util.UriComponentsBuilder;

import java.time.ZonedDateTime;
import java.util.Objects;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.stream.Stream;

import static com.pxdkxvan.auth.support.config.ApiConstants.*;
import static com.pxdkxvan.auth.support.config.TestConstants.ACCOUNT_EMAIL;
import static com.pxdkxvan.auth.support.config.TestConstants.ACCOUNT_USERNAME;

import static org.assertj.core.api.Assertions.assertThat;

@ImportTestcontainers(Containers.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration(initializers = DotenvApplicationContextInitializer.class)
@RequiredArgsConstructor(onConstructor_ = @Autowired, access = AccessLevel.PACKAGE)
@Import({TestEntityFactory.class, TestRequestDTOFactory.class, ResponseVerifier.class})
class RegisterIntegrationTest {

    @Value("http://localhost:${local.server.port}/")
    private String BASE_URL;

    private final RestOperations restOperations;

    private final JwtService jwtService;
    private final AccountService accountService;

    private final RoleRepository roleRepository;
    private final AccountRepository accountRepository;
    private final AuthorizationRepository authorizationRepository;
    private final VerificationRepository verificationRepository;

    private final RoleFactory roleFactory;
    private final TestEntityFactory testEntityFactory;
    private final TestRequestDTOFactory testRequestDTOFactory;
    private final RequestEntityFactory requestEntityFactory;

    private final UsernameGenerator usernameGenerator;
    private final ResponseVerifier responseVerifier;

    @BeforeEach
    void cleanUp() {
        Stream
                .of(verificationRepository, authorizationRepository, accountRepository, roleRepository)
                .forEach(CrudRepository::deleteAll);
    }

    private RequestEntity<?> buildVerificationRequest(VerificationMethod method, String key, String testTemporaryJwt) {
        if (method == VerificationMethod.LINK)
            return requestEntityFactory.createGet(
                    UriComponentsBuilder
                            .fromUriString(BASE_URL)
                            .path(VERIFICATION_LINK_ENDPOINT)
                            .queryParam(ConstantsConfig.VERIFICATION_LINK_PARAM_NAME, key)
                            .toUriString());

        CodeRequestDTO testCodeRequestDTO = testRequestDTOFactory.createCodeRequestDTO();
        testCodeRequestDTO.setCode(key);

        return requestEntityFactory.createPost(BASE_URL + VERIFICATION_CODE_ENDPOINT, testTemporaryJwt, testCodeRequestDTO);
    }

    @ParameterizedTest
    @EnumSource(VerificationMethod.class)
    void registerSuccess(VerificationMethod method) {
        Role testRole = roleFactory.create(ConstantsConfig.DEFAULT_ROLE_NAME);
        roleRepository.save(testRole);

        RegisterRequestDTO testRegisterRequestDTO = testRequestDTOFactory.createRegisterRequestDTO();

        RequestEntity<RegisterRequestDTO> testRegisterRequest =
                requestEntityFactory.createPost(BASE_URL + REGISTER_ENDPOINT, testRegisterRequestDTO);

        ResponseEntity<ResponseDTO<TokenResponseDTO>> testRegisterResponse =
                restOperations.exchange(testRegisterRequest, new ParameterizedTypeReference<>() {});

        responseVerifier.verifyNestedEntity(HttpStatus.OK, ResponseCode.ACCOUNT_CREATED, testRegisterResponse);

        String testTemporaryJwt = testRegisterResponse.getBody().getData().getToken();
        UUID testAccountId = UUID.fromString(jwtService.extractSubject(testTemporaryJwt));

        Account testAccount = accountService.findAccountById(testAccountId);
        assertThat(testAccount.getAuthorizations()).hasSize(1);

        String testMailingEndpoint = switch (method) {
            case LINK -> MAILING_LINK_ENDPOINT;
            case CODE -> MAILING_CODE_ENDPOINT;
        };

        RequestEntity<Void> testMailingRequest =
                requestEntityFactory.createGet(BASE_URL + testMailingEndpoint, testTemporaryJwt);

        ResponseEntity<ResponseDTO<Void>> testMailingResponse =
                restOperations.exchange(testMailingRequest, new ParameterizedTypeReference<>() {});

        responseVerifier.verifyPlainEntity(HttpStatus.OK, ResponseCode.VERIFICATION_EMAIL_SENT, testMailingResponse);

        testAccount = accountService.findAccountById(testAccountId);
        assertThat(testAccount.getVerifications()).hasSize(1);
        assertThat(verificationRepository.count()).isEqualTo(1);

        String token = verificationRepository
                .findByMethodAndAccount(method, testAccount)
                .orElseGet(() -> testEntityFactory.createVerification(method))
                .getKey();

        RequestEntity<?> testVerificationRequest =
                buildVerificationRequest(method, token, testTemporaryJwt);

        ResponseEntity<ResponseDTO<Void>> testVerificationResponse =
                restOperations.exchange(testVerificationRequest, new ParameterizedTypeReference<>() {});

        responseVerifier.verifyPlainEntity(HttpStatus.OK, ResponseCode.ACCOUNT_VERIFIED, testVerificationResponse);

        testAccount = accountService.findAccountById(testAccountId);
        assertThat(testAccount.getVerified()).isTrue();
        assertThat(testAccount.getVerifications()).isEmpty();
        assertThat(verificationRepository.count()).isEqualTo(0);
    }

    private Stream<Consumer<RegisterRequestDTO>> registerValidationArgs() {
        return Stream.of(
                dto -> dto.setUsername(null),
                dto -> dto.setUsername(""),
                dto -> dto.setUsername("***"),
                dto -> dto.setEmail(null),
                dto -> dto.setEmail(""),
                dto -> dto.setEmail("mail.ru"),
                dto -> dto.setEmail("testing@mail"),
                dto -> dto.setEmail("post@test.test"),
                dto -> dto.setEmail("postpostpostpostpostpostpostpostpost@test.test"),
                dto -> dto.setPassword(null),
                dto -> dto.setPassword("")
        );
    }

    @ParameterizedTest
    @MethodSource("registerValidationArgs")
    void registerFailedRequestNotValid(Consumer<RegisterRequestDTO> fieldSetter) {
        RegisterRequestDTO testRegisterRequestDTO = testRequestDTOFactory.createRegisterRequestDTO();
        fieldSetter.accept(testRegisterRequestDTO);

        RequestEntity<RegisterRequestDTO> testRegisterRequest =
                requestEntityFactory.createPost(BASE_URL + REGISTER_ENDPOINT, testRegisterRequestDTO);

        ResponseEntity<ResponseDTO<Void>> testRegisterResponse =
                restOperations.exchange(testRegisterRequest, new ParameterizedTypeReference<>() {});

        responseVerifier.verifyPlainEntity(HttpStatus.BAD_REQUEST, ResponseCode.VALIDATION_FAILED, testRegisterResponse);
    }

    @ParameterizedTest
    @NullAndEmptySource
    void registerFailedValidationCodeNotValid(String value) {
        String testJwt = jwtService.generateToken(JwtType.TEMPORARY, String.valueOf(UUID.randomUUID()));

        CodeRequestDTO testRegisterRequestDTO = testRequestDTOFactory.createCodeRequestDTO();
        testRegisterRequestDTO.setCode(value);

        RequestEntity<CodeRequestDTO> testVerificationRequest =
                requestEntityFactory.createPost(BASE_URL + VERIFICATION_CODE_ENDPOINT, testJwt, testRegisterRequestDTO);

        ResponseEntity<ResponseDTO<Void>> testVerificationResponse =
                restOperations.exchange(testVerificationRequest, new ParameterizedTypeReference<>() {});

        responseVerifier.verifyPlainEntity(HttpStatus.BAD_REQUEST, ResponseCode.VALIDATION_FAILED, testVerificationResponse);
    }

    @ParameterizedTest
    @ValueSource(strings = {ACCOUNT_USERNAME, ACCOUNT_EMAIL})
    void registerFailedLoginAlreadyTaken(String login) {
        Role testRole = roleFactory.create(ConstantsConfig.DEFAULT_ROLE_NAME);
        roleRepository.save(testRole);

        Account testAccount = testEntityFactory.createAccount();
        accountRepository.save(testAccount);

        RegisterRequestDTO testRegisterRequestDTO = testRequestDTOFactory.createRegisterRequestDTO();
        if (Objects.equals(login, ACCOUNT_EMAIL)) testRegisterRequestDTO.setUsername(usernameGenerator.random());

        RequestEntity<RegisterRequestDTO> testRegisterRequest =
                requestEntityFactory.createPost(BASE_URL + REGISTER_ENDPOINT, testRegisterRequestDTO);

        ResponseEntity<ResponseDTO<Void>> testRegisterResponse =
                restOperations.exchange(testRegisterRequest, new ParameterizedTypeReference<>() {});

        ResponseCode testRegisterResponseCode = Objects.equals(login, ACCOUNT_USERNAME) ?
                ResponseCode.USERNAME_ALREADY_TAKEN : ResponseCode.EMAIL_ALREADY_TAKEN;

        responseVerifier.verifyPlainEntity(HttpStatus.CONFLICT, testRegisterResponseCode, testRegisterResponse);
    }

    @Test
    void registerFailedRoleNotFound() {
        RegisterRequestDTO testRegisterRequestDTO = testRequestDTOFactory.createRegisterRequestDTO();

        RequestEntity<RegisterRequestDTO> testRegisterRequest =
                requestEntityFactory.createPost(BASE_URL + REGISTER_ENDPOINT, testRegisterRequestDTO);

        ResponseEntity<ResponseDTO<Void>> testRegisterResponse =
                restOperations.exchange(testRegisterRequest, new ParameterizedTypeReference<>() {});

        responseVerifier.verifyPlainEntity(HttpStatus.NOT_FOUND, ResponseCode.ROLE_NOT_FOUND, testRegisterResponse);
    }

    @ParameterizedTest
    @EnumSource(VerificationMethod.class)
    void registerFailedVerificationNotFound(VerificationMethod method) {
        String testTemporaryJwt = jwtService.generateToken(JwtType.TEMPORARY, String.valueOf(UUID.randomUUID()));

        String token = testEntityFactory
                .createVerification(method)
                .getKey();

        RequestEntity<?> testVerificationRequest =
                buildVerificationRequest(method, token, testTemporaryJwt);

        ResponseEntity<ResponseDTO<Void>> testVerificationResponse =
                restOperations.exchange(testVerificationRequest, new ParameterizedTypeReference<>() {});

        responseVerifier.verifyPlainEntity(HttpStatus.NOT_FOUND, ResponseCode.VERIFICATION_FAILED, testVerificationResponse);
    }

    @ParameterizedTest
    @EnumSource(VerificationMethod.class)
    void registerFailedVerificationExpired(VerificationMethod method) {
        Verification testVerification = testEntityFactory.createVerification(method);
        testVerification.setExpiresAt(ZonedDateTime.now());
        testVerification = verificationRepository.save(testVerification);

        Account testAccount = testVerification.getAccount();
        String testTemporaryJwt = jwtService.generateToken(JwtType.TEMPORARY, String.valueOf(testAccount.getId()));
        String key = testVerification.getKey();

        RequestEntity<?> testVerificationRequest =
                buildVerificationRequest(method, key, testTemporaryJwt);

        ResponseEntity<ResponseDTO<Void>> testVerificationResponse =
                restOperations.exchange(testVerificationRequest, new ParameterizedTypeReference<>() {});

        responseVerifier.verifyPlainEntity(HttpStatus.BAD_REQUEST, ResponseCode.VERIFICATION_FAILED, testVerificationResponse);
    }

}
