package com.pxkdxvan.auth.shared.builder;

import com.pxkdxvan.auth.shared.config.ConstantsConfig;
import com.pxkdxvan.auth.local.dto.TokenResponseDTO;
import com.pxkdxvan.auth.shared.dto.ResponseCode;
import com.pxkdxvan.auth.shared.dto.ResponseDTO;
import com.pxkdxvan.auth.shared.factory.CookieFactory;
import com.pxkdxvan.auth.shared.factory.ResponseDTOFactory;
import com.pxkdxvan.auth.shared.factory.ResponseEntityFactory;
import com.pxkdxvan.auth.shared.security.JwtType;
import com.pxkdxvan.auth.shared.service.JwtService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public class TokenResponseBuilder {

    private final JwtService jwtService;

    private final CookieFactory cookieFactory;
    private final ResponseDTOFactory responseDTOFactory;
    private final ResponseEntityFactory responseEntityFactory;

    private ResponseDTO<TokenResponseDTO> getTokenDTO(ResponseCode code, JwtType type, String jwtSubject) {
        String token = jwtService.generateToken(type, jwtSubject);
        return responseDTOFactory.createToken(token, code);
    }

    public ResponseEntity<ResponseDTO<TokenResponseDTO>> buildDual(String jwtSubject) {
        ResponseDTO<TokenResponseDTO> responseDTO = getTokenDTO(ResponseCode.ACCOUNT_LOGGED_IN, JwtType.ACCESS, jwtSubject);

        String refreshToken = jwtService.generateToken(JwtType.REFRESH, jwtSubject);
        ResponseCookie refreshCookie = cookieFactory.createDefault(ConstantsConfig.REFRESH_COOKIE_NAME, refreshToken);

        return responseEntityFactory.create(HttpStatus.OK, refreshCookie, responseDTO);
    }

    public ResponseEntity<ResponseDTO<TokenResponseDTO>> buildSingle(ResponseCode code, JwtType type, String jwtSubject) {
        ResponseDTO<TokenResponseDTO> responseDTO = getTokenDTO(code, type, jwtSubject);
        return responseEntityFactory.create(HttpStatus.OK, responseDTO);
    }

}
