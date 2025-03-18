package com.app85soft.qiqishop.dto.request.auth;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class VerifySessionReq {
    @NotBlank
    @JsonProperty("client_id")
    private String clientId;
    @NotBlank
    @JsonProperty("redirect_uri")
    private String redirectUri;
    @NotBlank
    @JsonProperty("code")
    private String sessionCode;
    @NotBlank
    @JsonProperty("client_secret")
    private String clientSecret;
    @JsonIgnore
    private String ip;

}