package com.pxkdxvan.auth.local.dto;

import jakarta.validation.constraints.NotBlank;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequestDTO {

    @NotBlank
    private String login;

    @NotBlank
    private String password;

}
