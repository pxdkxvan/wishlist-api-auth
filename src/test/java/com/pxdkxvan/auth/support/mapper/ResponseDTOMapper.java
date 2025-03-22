package com.pxdkxvan.auth.support.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.pxdkxvan.auth.dto.ResponseDTO;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

import org.springframework.boot.test.context.TestComponent;

@TestComponent
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public final class ResponseDTOMapper {

    private final ObjectMapper objectMapper;

    public <T> String toJSON(T dto) throws JsonProcessingException {
        return objectMapper.writeValueAsString(dto);
    }

    public <T> ResponseDTO<T> toDTO(String json, Class<T> clazz) throws JsonProcessingException {
        JavaType javaType = objectMapper.getTypeFactory().constructParametricType(ResponseDTO.class, clazz);
        return objectMapper.readValue(json, javaType);
    }

}
