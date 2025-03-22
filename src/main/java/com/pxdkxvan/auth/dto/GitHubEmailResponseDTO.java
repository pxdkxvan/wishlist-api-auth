package com.pxdkxvan.auth.dto;

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
