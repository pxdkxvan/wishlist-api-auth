package com.pxdkxvan.auth.support.util;

import com.pxdkxvan.auth.dto.ResponseDTO;
import com.pxdkxvan.auth.response.ResponseCode;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import org.springframework.boot.test.context.TestComponent;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

@TestComponent
@NoArgsConstructor(access = AccessLevel.PACKAGE)
public final class ResponseVerifier {

    private <T> void verifyEntity(HttpStatus expectedStatus, ResponseEntity<ResponseDTO<T>> responseEntity) {
        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getStatusCode()).isEqualTo(expectedStatus);
    }

    private <T> void verifyDTO(ResponseCode expectedCode, ResponseDTO<T> responseDTO) {
        assertThat(responseDTO).isNotNull();
        assertThat(responseDTO.getCode()).isEqualTo(expectedCode);
    }

    public void verifyPlainEntity(HttpStatus expectedStatus, ResponseCode expectedCode, ResponseEntity<ResponseDTO<Void>> responseEntity) {
        verifyEntity(expectedStatus, responseEntity);
        verifyPlainDTO(expectedCode, responseEntity.getBody());
    }

    public <T> void verifyNestedEntity(HttpStatus expectedStatus, ResponseCode expectedCode, ResponseEntity<ResponseDTO<T>> responseEntity) {
        verifyEntity(expectedStatus, responseEntity);
        verifyNestedDTO(expectedCode, responseEntity.getBody());
    }

    public void verifyPlainDTO(ResponseCode expectedCode, ResponseDTO<Void> statusResponseDTO) {
        verifyDTO(expectedCode, statusResponseDTO);
        assertThat(statusResponseDTO.getData()).isNull();
    }

    public <T> void verifyNestedDTO(ResponseCode expectedCode, ResponseDTO<T> statusResponseDTO) {
        verifyDTO(expectedCode, statusResponseDTO);
        assertThat(statusResponseDTO.getData()).isNotNull();
    }

}
