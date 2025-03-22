package com.pxdkxvan.auth.support.factory;

import com.pxdkxvan.auth.dto.ResponseDTO;
import com.pxdkxvan.auth.dto.TokenResponseDTO;
import com.pxdkxvan.auth.factory.CookieFactory;
import com.pxdkxvan.auth.factory.ResponseDTOFactory;
import com.pxdkxvan.auth.factory.ResponseEntityFactory;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestComponent;
import org.springframework.context.annotation.Import;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;

import static com.pxdkxvan.auth.support.config.TestConstants.*;

@TestComponent
@RequiredArgsConstructor(onConstructor_ = @Autowired, access = AccessLevel.PACKAGE)
@Import({ResponseEntityFactory.class, ResponseDTOFactory.class, CookieFactory.class})
public final class TestResponseEntityFactory {

    private final CookieFactory cookieFactory;
    private final ResponseDTOFactory responseDTOFactory;
    private final ResponseEntityFactory responseEntityFactory;

    public ResponseEntity<ResponseDTO<Void>> createStatus() {
        ResponseDTO<Void> testResponseDTO = responseDTOFactory.create(RESPONSE_CODE);
        return responseEntityFactory.create(RESPONSE_STATUS, testResponseDTO);
    }

    public ResponseEntity<ResponseDTO<TokenResponseDTO>> createSingleToken() {
        ResponseDTO<TokenResponseDTO> testResponseDTO = responseDTOFactory.createToken(RESPONSE_CODE, JWT_VALUE);
        return responseEntityFactory.create(RESPONSE_STATUS, testResponseDTO);
    }

    public ResponseEntity<ResponseDTO<TokenResponseDTO>> createDualToken() {
        ResponseCookie testResponseCookie = cookieFactory.createDefault(COOKIE_NAME, JWT_VALUE);
        ResponseDTO<TokenResponseDTO> testResponseDTO = responseDTOFactory.createToken(RESPONSE_CODE, JWT_VALUE);
        return responseEntityFactory.create(RESPONSE_STATUS, testResponseCookie, testResponseDTO);
    }

}
