package com.app85soft.qiqishop.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;


@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class RecaptchaResponse {
    @JsonProperty("challenge_ts")
    private String challengeTs;
    @JsonProperty("error-codes")
    private List<String> errorCodes;
    private String hostname;
    private Boolean success;

}
