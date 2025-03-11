package com.pxkdxvan.auth.local.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TokenResponseDTO {
    private String token;
}
