package com.pxdkxvan.auth.service;

import com.pxdkxvan.auth.dto.ResponseDTO;
import com.pxdkxvan.auth.dto.TokenResponseDTO;
import com.pxdkxvan.auth.factory.CookieFactory;
import com.pxdkxvan.auth.factory.ResponseDTOFactory;
import com.pxdkxvan.auth.factory.ResponseEntityFactory;
import com.pxdkxvan.auth.response.StatusResponseShaper;
import com.pxdkxvan.auth.response.TokenResponseShaper;
import com.pxdkxvan.auth.security.JwtType;
import com.pxdkxvan.auth.support.util.ResponseVerifier;
import com.pxdkxvan.auth.support.util.TestRandomizer;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.util.UUID;

import static com.pxdkxvan.auth.support.config.TestConstants.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@RequiredArgsConstructor(onConstructor_ = @Autowired, access = AccessLevel.PACKAGE)
@SpringJUnitConfig(classes = {TokenResponseShaper.class, StatusResponseShaper.class, ResponseEntityFactory.class,
        ResponseDTOFactory.class, CookieFactory.class, ResponseVerifier.class, TestRandomizer.class})
public class ShaperTest {

    @MockitoBean
    private final JwtService jwtService;

    private final StatusResponseShaper statusResponseShaper;
    private final TokenResponseShaper tokenResponseShaper;

    private final ResponseVerifier responseVerifier;

    private final TestRandomizer testRandomizer;

    @Test
    public void buildStatusResponseSuccess() {
        ResponseEntity<ResponseDTO<Void>> testResponse = statusResponseShaper.buildResponse(RESPONSE_STATUS, RESPONSE_CODE);
        responseVerifier.verifyPlainEntity(RESPONSE_STATUS, RESPONSE_CODE, testResponse);
    }

    @Test
    public void buildSingleTokenResponseSuccess() {
        String testAccountId = String.valueOf(UUID.randomUUID());
        JwtType testJwtType = testRandomizer.random(JwtType.values());

        when(jwtService.generateToken(any(JwtType.class), eq(testAccountId))).thenReturn(JWT_VALUE);

        ResponseEntity<ResponseDTO<TokenResponseDTO>> testResponse = tokenResponseShaper.buildSingle(RESPONSE_CODE, testJwtType, testAccountId);
        responseVerifier.verifyNestedEntity(HttpStatus.OK, RESPONSE_CODE, testResponse);
        assertThat(testResponse.getBody().getData().getToken()).isEqualTo(JWT_VALUE);

        verify(jwtService).generateToken(any(JwtType.class), eq(testAccountId));
    }

    @Test
    public void buildDualTokenResponseSuccess() {
        String testAccountId = String.valueOf(UUID.randomUUID());

        when(jwtService.generateToken(any(JwtType.class), eq(testAccountId))).thenReturn(JWT_VALUE);

        ResponseEntity<ResponseDTO<TokenResponseDTO>> testResponse = tokenResponseShaper.buildDual(RESPONSE_CODE, testAccountId);
        responseVerifier.verifyNestedEntity(HttpStatus.OK, RESPONSE_CODE, testResponse);
        assertThat(testResponse.getHeaders().getFirst(HttpHeaders.SET_COOKIE)).contains(JWT_VALUE);
        assertThat(testResponse.getBody().getData().getToken()).isEqualTo(JWT_VALUE);

        verify(jwtService, times(2)).generateToken(any(JwtType.class), eq(testAccountId));
    }

}
