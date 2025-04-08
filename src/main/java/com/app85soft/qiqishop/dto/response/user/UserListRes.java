package com.app85soft.qiqishop.dto.response.user;

import com.app85soft.qiqishop.dto.constant.ActiveStatus;
import com.app85soft.qiqishop.dto.response.role.RoleDetail;
import com.app85soft.qiqishop.entities.user.constant.Gender;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.sql.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserListRes {
    int id;
    String code;
    String name;
    String email;
    String phone;
    ActiveStatus status;
    Date birthday;
    Gender gender;
    RoleDetail role;
}
