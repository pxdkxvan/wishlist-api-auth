package com.pxdkxvan.auth.factory;

import com.pxdkxvan.auth.dto.TokenResponseDTO;
import com.pxdkxvan.auth.response.ResponseCode;
import com.pxdkxvan.auth.dto.ResponseDTO;

import org.springframework.stereotype.Component;

@Component
public final class ResponseDTOFactory {

    public <T> ResponseDTO<T> create(ResponseCode code) {
        return ResponseDTO
                .<T>builder()
                .code(code)
                .build();
    }

    public <T> ResponseDTO<T> create(ResponseCode code, T data) {
        return ResponseDTO
                .<T>builder()
                .code(code)
                .data(data)
                .build();
    }

    public ResponseDTO<TokenResponseDTO> createToken(ResponseCode code, String token) {
        TokenResponseDTO tokenResponseDTO = TokenResponseDTO
                .builder()
                .token(token)
                .build();

        return create(code, tokenResponseDTO);
    }

}
