package com.app85soft.qiqishop.dto.response.permission;

import com.app85soft.qiqishop.entities.role.constant.PermissionGroup;
import com.app85soft.qiqishop.entities.role.constant.PermissionType;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PermissionRes {
    int id;
    String title;
    PermissionType permission;
    PermissionGroup parentPermission;
    Boolean isView;
    Boolean isWrite;
    Boolean isApproval;
    Boolean isDecision;

}
