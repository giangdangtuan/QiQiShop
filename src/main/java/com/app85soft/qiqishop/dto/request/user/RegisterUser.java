package com.app85soft.qiqishop.dto.request.user;

import com.app85soft.qiqishop.dto.constant.RoleType;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RegisterUser {
    @NotBlank
    String name;
    String phone;
    String email;

    @NotBlank
    String password;
    @NotBlank
    String confirmPassword;

    @NotNull
    Integer codeId;
    @NotBlank
    String code;

    @AssertTrue(message = "Password don't matching")
    public boolean isValid() {
        return password.equals(confirmPassword);
    }

}
