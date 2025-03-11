package com.pxkdxvan.auth.local.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.time.ZonedDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CaptchaRequestDTO {
    private Boolean success;
    @JsonProperty("challenge_ts") private ZonedDateTime challengeTs;
    private String hostname;
    @JsonProperty("error-codes") private List<String> errorCodes;
}
