package com.pxkdxvan.auth.shared.factory;

import com.pxkdxvan.auth.local.dto.TokenResponseDTO;

import com.pxkdxvan.auth.shared.dto.ResponseCode;
import com.pxkdxvan.auth.shared.dto.ResponseDTO;
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

    public ResponseDTO<TokenResponseDTO> createToken(String token, ResponseCode code) {
        TokenResponseDTO tokenResponseDTO = TokenResponseDTO
                .builder()
                .token(token)
                .build();

        return create(code, tokenResponseDTO);
    }

}
