package com.pxkdxvan.auth.oauth2.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OAuth2AccountData {
    private String username;
    private String email;
    private String providerId;
}
