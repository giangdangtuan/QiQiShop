package com.app85soft.qiqishop.dto.request.user;

import com.app85soft.qiqishop.entities.user.constant.Gender;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;

import java.sql.Date;

@Getter
@Setter
@FieldNameConstants(level = AccessLevel.PRIVATE)
public class EditMyProfileReq {
    @NotBlank
    String name;
    Date birthday;
    Gender gender;
    Integer avatarId;

}
