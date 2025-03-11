package com.pxkdxvan.auth.oauth2.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GitHubEmailResponseDTO {
    private String email;
    private boolean primary;
//    private boolean verified;  IF SEVERAL EMAILS
}
