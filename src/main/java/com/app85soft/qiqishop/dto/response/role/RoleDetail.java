package com.app85soft.qiqishop.dto.response.role;

import com.app85soft.qiqishop.dto.constant.RoleType;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RoleDetail {
    Integer roleId;
    String roleName;
    RoleType roleType;

}
