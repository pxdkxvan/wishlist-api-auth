package com.pxdkxvan.auth.model;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class OAuth2AccountData {
    private String username;
    private String email;
    private String providerId;
}
