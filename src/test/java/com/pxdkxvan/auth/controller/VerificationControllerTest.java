package com.pxdkxvan.auth.controller;

import com.pxdkxvan.auth.config.ConstantsConfig;
import com.pxdkxvan.auth.response.ResponseCode;
import com.pxdkxvan.auth.support.factory.TestRequestDTOFactory;
import com.pxdkxvan.auth.dto.CodeRequestDTO;
import com.pxdkxvan.auth.dto.ResponseDTO;
import com.pxdkxvan.auth.response.StatusResponseShaper;
import com.pxdkxvan.auth.service.JwtService;
import com.pxdkxvan.auth.service.VerificationService;
import com.pxdkxvan.auth.support.factory.TestResponseEntityFactory;
import com.pxdkxvan.auth.support.mapper.ResponseDTOMapper;
import com.pxdkxvan.auth.support.util.MockMvcPerformer;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Collections;
import java.util.Map;
import java.util.UUID;

import static com.pxdkxvan.auth.support.config.ApiConstants.*;
import static com.pxdkxvan.auth.support.config.TestConstants.*;

import static org.assertj.core.api.Assertions.assertThat;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RequiredArgsConstructor(onConstructor_ = @Autowired, access = AccessLevel.PACKAGE)
@Import({MockMvcPerformer.class, ResponseDTOMapper.class, TestResponseEntityFactory.class, TestRequestDTOFactory.class})
@WebMvcTest(value = VerificationController.class,
        excludeFilters = @ComponentScan.Filter(type = FilterType.ANNOTATION, classes = RestControllerAdvice.class))
class VerificationControllerTest {

    private final MockMvcPerformer mockMvcPerformer;

    @MockitoBean
    private final JwtService jwtService;
    @MockitoBean
    private final VerificationService verificationService;

    @MockitoBean
    private final StatusResponseShaper statusResponseShaper;

    private final ResponseDTOMapper responseDtoMapper;

    private final TestRequestDTOFactory testRequestDTOFactory;
    private final TestResponseEntityFactory testResponseEntityFactory;

    @Test
    @WithMockUser
    void verifyLinkSuccess() throws Exception {
        ResponseEntity<ResponseDTO<Void>> testExpectedResponse = testResponseEntityFactory.createStatus();
        when(statusResponseShaper.buildResponse(HttpStatus.OK, ResponseCode.ACCOUNT_VERIFIED)).thenReturn(testExpectedResponse);

        Map<String, String> testGetParams = Collections.singletonMap(ConstantsConfig.VERIFICATION_LINK_PARAM_NAME, VERIFICATION_TOKEN);
        MvcResult testResult = mockMvcPerformer.performWithGetRequest(VERIFICATION_LINK_ENDPOINT, testGetParams);
        ResponseDTO<Void> testResponseDTO = responseDtoMapper.toDTO(testResult.getResponse().getContentAsString(), Void.class);
        assertThat(testExpectedResponse.getBody()).isEqualTo(testResponseDTO);

        verify(verificationService).verifyAccountWithToken(VERIFICATION_TOKEN);
        verify(statusResponseShaper).buildResponse(HttpStatus.OK, ResponseCode.ACCOUNT_VERIFIED);
    }

    @Test
    void verifyCodeSuccess() throws Exception {
        UUID testAccountId = UUID.randomUUID();

        when(jwtService.extractSubject(JWT_VALUE)).thenReturn(String.valueOf(testAccountId));

        ResponseEntity<ResponseDTO<Void>> testExpectedResponse = testResponseEntityFactory.createStatus();
        when(statusResponseShaper.buildResponse(RESPONSE_STATUS, ResponseCode.ACCOUNT_VERIFIED)).thenReturn(testExpectedResponse);

        CodeRequestDTO codeRequestDTO = testRequestDTOFactory.createCodeRequestDTO();
        MvcResult testResult = mockMvcPerformer.performWithPostRequestAndJwtBearer(VERIFICATION_CODE_ENDPOINT, codeRequestDTO, JWT_VALUE);
        ResponseDTO<Void> testResponseDTO = responseDtoMapper.toDTO(testResult.getResponse().getContentAsString(), Void.class);
        assertThat(testExpectedResponse.getBody()).isEqualTo(testResponseDTO);

        verify(jwtService).extractSubject(JWT_VALUE);
        verify(verificationService).verifyAccountWithCode(VERIFICATION_CODE, testAccountId);
        verify(statusResponseShaper).buildResponse(RESPONSE_STATUS, ResponseCode.ACCOUNT_VERIFIED);
    }

}
