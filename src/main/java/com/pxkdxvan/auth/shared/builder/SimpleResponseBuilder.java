package com.pxkdxvan.auth.shared.builder;

import com.pxkdxvan.auth.shared.dto.ResponseCode;
import com.pxkdxvan.auth.shared.dto.ResponseDTO;
import com.pxkdxvan.auth.shared.factory.ResponseDTOFactory;
import com.pxkdxvan.auth.shared.factory.ResponseEntityFactory;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public class SimpleResponseBuilder {

    private final ResponseDTOFactory responseDTOFactory;
    private final ResponseEntityFactory responseEntityFactory;

    public ResponseEntity<ResponseDTO<Void>> buildResponse(HttpStatus status, ResponseCode code) {
        ResponseDTO<Void> responseDTO = responseDTOFactory.create(code);
        return responseEntityFactory.create(status, responseDTO);
    }

}
