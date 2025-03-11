package com.pxkdxvan.auth.shared.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseDTO<T> {
    private ResponseCode code;
    private T data;
}
