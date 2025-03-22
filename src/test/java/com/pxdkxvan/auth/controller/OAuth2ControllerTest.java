package com.pxdkxvan.auth.controller;

import com.pxdkxvan.auth.config.ConstantsConfig;
import com.pxdkxvan.auth.dto.ResponseDTO;
import com.pxdkxvan.auth.dto.TokenResponseDTO;
import com.pxdkxvan.auth.response.ResponseCode;
import com.pxdkxvan.auth.response.TokenResponseShaper;
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
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Collections;
import java.util.Map;
import java.util.UUID;

import static com.pxdkxvan.auth.support.config.TestConstants.JWT_VALUE;
import static com.pxdkxvan.auth.support.config.ApiConstants.OAUTH2_CALLBACK_ENDPOINT;

import static org.assertj.core.api.Assertions.assertThat;

import static org.mockito.Mockito.*;

@RequiredArgsConstructor(onConstructor_ = @Autowired, access = AccessLevel.PACKAGE)
@Import({MockMvcPerformer.class, TestResponseEntityFactory.class, ResponseDTOMapper.class})
@WebMvcTest(value = OAuth2AuthController.class,
        excludeFilters = @ComponentScan.Filter(type = FilterType.ANNOTATION, classes = RestControllerAdvice.class))
public class OAuth2ControllerTest {

    private final MockMvcPerformer mockMvcPerformer;

    @MockitoBean
    private final TokenResponseShaper tokenResponseShaper;

    private final ResponseDTOMapper responseDtoMapper;

    private final TestResponseEntityFactory testResponseEntityFactory;

    @Test
    void callbackSuccess() throws Exception {
        UUID testAccountId = UUID.randomUUID();
        String testAccountIdString = String.valueOf(testAccountId);

        ResponseEntity<ResponseDTO<TokenResponseDTO>> testExpectedResponse = testResponseEntityFactory.createDualToken();
        when(tokenResponseShaper.buildDual(ResponseCode.ACCOUNT_LOGGED_IN, testAccountIdString)).thenReturn(testExpectedResponse);

        Map<String, Object> testOAuth2Attrs = Collections.singletonMap(ConstantsConfig.ACCOUNT_ID_ATTR_NAME, testAccountId);
        MvcResult testResult = mockMvcPerformer.performWithOAuth2GetRequestAndResponseCookie(OAUTH2_CALLBACK_ENDPOINT, testOAuth2Attrs, JWT_VALUE);
        ResponseDTO<TokenResponseDTO> testResponseDTO = responseDtoMapper.toDTO(testResult.getResponse().getContentAsString(), TokenResponseDTO.class);
        assertThat(testExpectedResponse.getBody()).isEqualTo(testResponseDTO);

        verify(tokenResponseShaper).buildDual(ResponseCode.ACCOUNT_LOGGED_IN, testAccountIdString);
    }

}
