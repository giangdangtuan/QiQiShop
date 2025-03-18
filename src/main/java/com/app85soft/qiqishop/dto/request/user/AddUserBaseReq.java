package com.app85soft.qiqishop.dto.request.user;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
//@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AddUserBaseReq {
    @NotBlank
    String name;
    String phone;
    @NotBlank
    String email;
    String address;
    @NotBlank
    String password;
    @NotBlank
    String confirmPassword;
    @NotNull
    Integer roleId;

    @AssertTrue(message = "Password don't matching")
    public boolean isValid() {
        return password.equals(confirmPassword);
    }
}
