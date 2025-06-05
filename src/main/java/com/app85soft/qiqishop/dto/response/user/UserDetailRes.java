package com.app85soft.qiqishop.dto.response.user;

import com.app85soft.qiqishop.dto.constant.ActiveStatus;
import com.app85soft.qiqishop.dto.response.file.UploadFileRes;
import com.app85soft.qiqishop.dto.response.permission.PermissionRes;
import com.app85soft.qiqishop.dto.response.role.RoleDetail;
import com.app85soft.qiqishop.entities.user.constant.Gender;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserDetailRes {
    int id;
    String code;
    String phone;
    String name;
    String email;
    Date birthday;
    Gender gender;
    Integer avatarId;
    ActiveStatus status;
    RoleDetail role;
    List<PermissionRes> permissions;
    String accessToken;
    UploadFileRes avatar;
}
