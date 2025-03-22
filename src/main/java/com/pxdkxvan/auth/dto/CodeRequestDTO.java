package com.pxdkxvan.auth.dto;

import jakarta.validation.constraints.NotBlank;

import lombok.*;

import org.hibernate.validator.constraints.Length;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CodeRequestDTO {
    @NotBlank
    @Length(min = 6, max = 6)
    private String code;
}
