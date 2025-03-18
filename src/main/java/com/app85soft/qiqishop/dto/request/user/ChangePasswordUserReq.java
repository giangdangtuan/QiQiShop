package com.app85soft.qiqishop.dto.request.user;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ChangePasswordUserReq {
    @NotNull
    int userId;
    @NotBlank
    String newPassword;
    @NotBlank
    String confirmPassword;

    @AssertTrue(message = "Password don't matching")
    public boolean isValid() {
        return newPassword.equals(confirmPassword);
    }
}