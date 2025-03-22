package com.pxdkxvan.auth.response;

import com.pxdkxvan.auth.config.ConstantsConfig;
import com.pxdkxvan.auth.dto.TokenResponseDTO;
import com.pxdkxvan.auth.dto.ResponseDTO;
import com.pxdkxvan.auth.factory.CookieFactory;
import com.pxdkxvan.auth.factory.ResponseDTOFactory;
import com.pxdkxvan.auth.factory.ResponseEntityFactory;
import com.pxdkxvan.auth.security.JwtType;
import com.pxdkxvan.auth.service.JwtService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public class TokenResponseShaper {

    private final JwtService jwtService;

    private final CookieFactory cookieFactory;
    private final ResponseDTOFactory responseDTOFactory;
    private final ResponseEntityFactory responseEntityFactory;

    private ResponseDTO<TokenResponseDTO> getTokenDTO(ResponseCode code, JwtType type, String jwtSubject) {
        String token = jwtService.generateToken(type, jwtSubject);
        return responseDTOFactory.createToken(code, token);
    }

    public ResponseEntity<ResponseDTO<TokenResponseDTO>> buildDual(ResponseCode code, String jwtSubject) {
        ResponseDTO<TokenResponseDTO> responseDTO = getTokenDTO(code, JwtType.ACCESS, jwtSubject);

        String refreshToken = jwtService.generateToken(JwtType.REFRESH, jwtSubject);
        ResponseCookie refreshCookie = cookieFactory.createDefault(ConstantsConfig.REFRESH_COOKIE_NAME, refreshToken);

        return responseEntityFactory.create(HttpStatus.OK, refreshCookie, responseDTO);
    }

    public ResponseEntity<ResponseDTO<TokenResponseDTO>> buildSingle(ResponseCode code, JwtType type, String jwtSubject) {
        ResponseDTO<TokenResponseDTO> responseDTO = getTokenDTO(code, type, jwtSubject);
        return responseEntityFactory.create(HttpStatus.OK, responseDTO);
    }

}
