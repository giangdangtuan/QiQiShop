package com.app85soft.qiqishop.dto.request.user;

import com.app85soft.qiqishop.dto.constant.ActiveStatus;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;

@Getter
@Setter
@FieldNameConstants(level = AccessLevel.PRIVATE)
public class UpdateUserReq extends EditMyProfileReq{
    @NotNull
    int userId;
    @NotBlank
    String phone;
    @Email
    String email;
    Integer roleId;
    ActiveStatus status;

}