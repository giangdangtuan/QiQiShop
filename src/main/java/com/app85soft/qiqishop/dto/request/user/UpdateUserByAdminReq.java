package com.app85soft.qiqishop.dto.request.user;

import com.app85soft.qiqishop.dto.constant.ActiveStatus;
import com.app85soft.qiqishop.entities.user.constant.Gender;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.sql.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpdateUserByAdminReq {
    @NotNull
    int userId;
    Integer avatarId;
    @NotBlank
    String name;
    @NotBlank
    String phone;
    String email;
    Gender gender;
    Date birthday;
    ActiveStatus status;
    Integer roleId;
}
