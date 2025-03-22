package com.pxdkxvan.auth.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import com.pxdkxvan.auth.response.ResponseCode;

import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseDTO<T> {
    private ResponseCode code;
    private T data;
}
