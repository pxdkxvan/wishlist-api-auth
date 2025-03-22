package com.pxdkxvan.auth.response;

import com.pxdkxvan.auth.dto.ResponseDTO;
import com.pxdkxvan.auth.factory.ResponseDTOFactory;
import com.pxdkxvan.auth.factory.ResponseEntityFactory;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public class StatusResponseShaper {

    private final ResponseDTOFactory responseDTOFactory;
    private final ResponseEntityFactory responseEntityFactory;

    public ResponseEntity<ResponseDTO<Void>> buildResponse(HttpStatus status, ResponseCode code) {
        ResponseDTO<Void> responseDTO = responseDTOFactory.create(code);
        return responseEntityFactory.create(status, responseDTO);
    }

}
