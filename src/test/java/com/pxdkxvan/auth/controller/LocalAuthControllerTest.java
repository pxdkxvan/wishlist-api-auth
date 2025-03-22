package com.pxdkxvan.auth.controller;

import com.pxdkxvan.auth.config.ConstantsConfig;
import com.pxdkxvan.auth.factory.CookieFactory;
import com.pxdkxvan.auth.response.ResponseCode;
import com.pxdkxvan.auth.service.JwtService;
import com.pxdkxvan.auth.support.factory.TestRequestDTOFactory;
import com.pxdkxvan.auth.support.factory.TestEntityFactory;
import com.pxdkxvan.auth.dto.LoginRequestDTO;
import com.pxdkxvan.auth.dto.RegisterRequestDTO;
import com.pxdkxvan.auth.dto.ResponseDTO;
import com.pxdkxvan.auth.dto.TokenResponseDTO;
import com.pxdkxvan.auth.model.Account;
import com.pxdkxvan.auth.response.TokenResponseShaper;
import com.pxdkxvan.auth.security.JwtType;
import com.pxdkxvan.auth.service.LocalAuthService;
import com.pxdkxvan.auth.support.factory.TestResponseEntityFactory;
import com.pxdkxvan.auth.support.mapper.CookieMapper;
import com.pxdkxvan.auth.support.mapper.ResponseDTOMapper;
import com.pxdkxvan.auth.support.util.MockMvcPerformer;

import jakarta.servlet.http.Cookie;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.UUID;

import static com.pxdkxvan.auth.support.config.TestConstants.*;
import static com.pxdkxvan.auth.support.config.ApiConstants.*;

import static org.assertj.core.api.Assertions.assertThat;

import static org.mockito.Mockito.*;

@AutoConfigureMockMvc(addFilters = false)
@RequiredArgsConstructor(onConstructor_ = @Autowired, access = AccessLevel.PACKAGE)
@Import({MockMvcPerformer.class, CookieFactory.class, CookieMapper.class, ResponseDTOMapper.class,
        TestEntityFactory.class, TestResponseEntityFactory.class, TestRequestDTOFactory.class})
@WebMvcTest(value = LocalAuthController.class,
        excludeFilters = @ComponentScan.Filter(type = FilterType.ANNOTATION, classes = RestControllerAdvice.class))
class LocalAuthControllerTest {

    private final MockMvcPerformer mockMvcPerformer;

    @MockitoBean
    private final JwtService jwtService;
    @MockitoBean
    private final LocalAuthService localAuthService;

    @MockitoBean
    private final TokenResponseShaper tokenResponseShaper;

    private final CookieMapper cookieMapper;
    private final ResponseDTOMapper responseDtoMapper;

    private final CookieFactory cookieFactory;
    private final TestEntityFactory testEntityFactory;
    private final TestRequestDTOFactory testRequestDTOFactory;
    private final TestResponseEntityFactory testResponseEntityFactory;

    @ParameterizedTest
    @ValueSource(strings = {ACCOUNT_USERNAME, ACCOUNT_EMAIL})
    public void loginSuccess(String login) throws Exception {
        Account testAccount = testEntityFactory.createIdentifiedAccount();
        String testAccountId = String.valueOf(testAccount.getId());
        when(localAuthService.login(login, ACCOUNT_PASSWORD)).thenReturn(testAccount);

        ResponseEntity<ResponseDTO<TokenResponseDTO>> testExpectedResponse = testResponseEntityFactory.createDualToken();
        when(tokenResponseShaper.buildDual(ResponseCode.ACCOUNT_LOGGED_IN, testAccountId)).thenReturn(testExpectedResponse);

        LoginRequestDTO testRequestDTO = testRequestDTOFactory.createLoginRequestDTO(login);
        MvcResult testResult = mockMvcPerformer.performWithPostRequestAndResponseCookie(LOGIN_ENDPOINT, testRequestDTO, JWT_VALUE);
        ResponseDTO<TokenResponseDTO> testResponseDTO = responseDtoMapper.toDTO(testResult.getResponse().getContentAsString(), TokenResponseDTO.class);
        assertThat(testExpectedResponse.getBody()).isEqualTo(testResponseDTO);

        verify(localAuthService).login(login, ACCOUNT_PASSWORD);
        verify(tokenResponseShaper).buildDual(ResponseCode.ACCOUNT_LOGGED_IN, testAccountId);
    }

    @Test
    void registerSuccess() throws Exception {
        Account testAccount = testEntityFactory.createIdentifiedAccount();
        String testAccountId = String.valueOf(testAccount.getId());
        when(localAuthService.register(ACCOUNT_USERNAME, ACCOUNT_EMAIL, ACCOUNT_PASSWORD)).thenReturn(testAccount);

        ResponseEntity<ResponseDTO<TokenResponseDTO>> testExpectedResponse = testResponseEntityFactory.createDualToken();
        when(tokenResponseShaper.buildSingle(ResponseCode.ACCOUNT_CREATED, JwtType.TEMPORARY, testAccountId)).thenReturn(testExpectedResponse);

        RegisterRequestDTO testRequestDTO = testRequestDTOFactory.createRegisterRequestDTO();
        MvcResult testResult = mockMvcPerformer.performWithPostRequest(REGISTER_ENDPOINT, testRequestDTO);
        ResponseDTO<TokenResponseDTO> testResponseDTO = responseDtoMapper.toDTO(testResult.getResponse().getContentAsString(), TokenResponseDTO.class);
        assertThat(testExpectedResponse.getBody()).isEqualTo(testResponseDTO);

        verify(localAuthService).register(ACCOUNT_USERNAME, ACCOUNT_EMAIL, ACCOUNT_PASSWORD);
        verify(tokenResponseShaper).buildSingle(ResponseCode.ACCOUNT_CREATED, JwtType.TEMPORARY, testAccountId);
    }

    @Test
    void refreshSuccess() throws Exception {
        String testAccountId = String.valueOf(UUID.randomUUID());

        when(jwtService.extractSubject(JWT_VALUE)).thenReturn(testAccountId);

        ResponseEntity<ResponseDTO<TokenResponseDTO>> testExpectedResponse = testResponseEntityFactory.createSingleToken();
        when(tokenResponseShaper.buildSingle(ResponseCode.TOKEN_REFRESHED, JwtType.ACCESS, testAccountId)).thenReturn(testExpectedResponse);

        ResponseCookie testSpringCookie = cookieFactory.createDefault(ConstantsConfig.REFRESH_COOKIE_NAME, JWT_VALUE);
        Cookie testJakartaCookie = cookieMapper.toCookie(testSpringCookie);
        MvcResult testResult = mockMvcPerformer.performWithPostRequestAndRequestCookie(REFRESH_ENDPOINT, testJakartaCookie);
        ResponseDTO<TokenResponseDTO> testResponseDTO = responseDtoMapper.toDTO(testResult.getResponse().getContentAsString(), TokenResponseDTO.class);
        assertThat(testExpectedResponse.getBody()).isEqualTo(testResponseDTO);

        verify(jwtService).extractSubject(JWT_VALUE);
        verify(tokenResponseShaper).buildSingle(ResponseCode.TOKEN_REFRESHED, JwtType.ACCESS, testAccountId);
    }

}
