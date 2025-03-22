package com.pxdkxvan.auth.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequestDTO implements Serializable {

    @NotBlank
    @Pattern(regexp = "^[a-zA-Z0-9._-]+$")
    private String username;

    @NotBlank
    @Pattern(regexp = "^.{6,30}@.+\\..+$")
    private String email;

    @NotBlank
    private String password;

}
