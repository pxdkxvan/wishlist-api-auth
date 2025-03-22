package com.pxdkxvan.auth.controller;

import com.pxdkxvan.auth.dto.ResponseDTO;
import com.pxdkxvan.auth.model.VerificationMethod;
import com.pxdkxvan.auth.response.ResponseCode;
import com.pxdkxvan.auth.response.StatusResponseShaper;
import com.pxdkxvan.auth.service.MailingService;
import com.pxdkxvan.auth.support.factory.TestResponseEntityFactory;
import com.pxdkxvan.auth.support.mapper.ResponseDTOMapper;
import com.pxdkxvan.auth.support.util.MockMvcPerformer;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static com.pxdkxvan.auth.support.config.ApiConstants.*;
import static com.pxdkxvan.auth.support.config.TestConstants.JWT_VALUE;

import static com.pxdkxvan.auth.support.config.TestConstants.RESPONSE_STATUS;
import static org.assertj.core.api.Assertions.assertThat;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RequiredArgsConstructor(onConstructor_ = @Autowired, access = AccessLevel.PACKAGE)
@Import({MockMvcPerformer.class, TestResponseEntityFactory.class, ResponseDTOMapper.class})
@WebMvcTest(value = MailingController.class,
        excludeFilters = @ComponentScan.Filter(type = FilterType.ANNOTATION, classes = RestControllerAdvice.class))
class MailingControllerTest {

    private final MockMvcPerformer mockMvcPerformer;

    @MockitoBean
    private final MailingService mailingService;

    @MockitoBean
    private final StatusResponseShaper statusResponseShaper;

    private final TestResponseEntityFactory testResponseEntityFactory;

    private final ResponseDTOMapper responseDtoMapper;

    @ParameterizedTest
    @EnumSource(VerificationMethod.class)
    void sendVerificationEmailSuccess(VerificationMethod method) throws Exception {
        String testUri = switch (method) {
            case LINK -> MAILING_LINK_ENDPOINT;
            case CODE -> MAILING_CODE_ENDPOINT;
        };

        ResponseEntity<ResponseDTO<Void>> testExpectedResponse = testResponseEntityFactory.createStatus();
        when(statusResponseShaper.buildResponse(RESPONSE_STATUS, ResponseCode.VERIFICATION_EMAIL_SENT)).thenReturn(testExpectedResponse);

        MvcResult testResult = mockMvcPerformer.performWithGetRequestAndJwtBearer(testUri, JWT_VALUE);
        ResponseDTO<Void> testResponseDTO = responseDtoMapper.toDTO(testResult.getResponse().getContentAsString(), Void.class);
        assertThat(testExpectedResponse.getBody()).isEqualTo(testResponseDTO);

        verify(mailingService).sendVerificationEmail(method, JWT_VALUE);
        verify(statusResponseShaper).buildResponse(RESPONSE_STATUS, ResponseCode.VERIFICATION_EMAIL_SENT);
    }

}
