package com.app85soft.qiqishop.dto.request.auth;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ForgotPasswordReq {
    @NotNull
    int codeId;
    @NotBlank
    String code;
    @NotBlank
    String newPassword;
    @NotBlank
    String confirmNewPassword;

    @AssertTrue(message = "Password don't matching")
    public boolean isValid() {
        return newPassword.equals(confirmNewPassword);
    }
}
