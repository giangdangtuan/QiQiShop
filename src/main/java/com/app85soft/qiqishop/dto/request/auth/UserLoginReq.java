package com.app85soft.qiqishop.dto.request.auth;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UserLoginReq {
    @NotBlank
    private String phone;
    @NotBlank
    private String password;
}
