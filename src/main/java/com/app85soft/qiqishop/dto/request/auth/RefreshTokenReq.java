package com.app85soft.qiqishop.dto.request.auth;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RefreshTokenReq {
    @NotBlank
    @JsonProperty("client_id")
    private String clientId;
    @NotBlank
    @JsonProperty("client_secret")
    private String clientSecret;
    @NotBlank
    @JsonProperty("refresh_token")
    private String refreshToken;
    @JsonIgnore
    private String ip;

}