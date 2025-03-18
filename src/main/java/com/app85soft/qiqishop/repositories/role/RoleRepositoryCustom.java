package com.app85soft.qiqishop.repositories.role;


import com.app85soft.qiqishop.dto.constant.ActiveStatus;
import com.app85soft.qiqishop.dto.constant.RoleType;
import com.app85soft.qiqishop.dto.response.permission.PermissionRes;
import com.app85soft.qiqishop.dto.response.role.RoleDetail;
import com.app85soft.qiqishop.dto.response.role.RoleRes;
import com.app85soft.qiqishop.entities.role.Role;
import com.app85soft.qiqishop.entities.role.constant.PermissionKey;
import com.app85soft.qiqishop.entities.role.constant.PermissionType;

import java.util.List;

public interface RoleRepositoryCustom {
    List<Integer> getAllIdToCheckExist(Role role, List<Integer> ids);

    void deleteRoleByIds(List<Integer> ids);

    long countRoles(Role role, ActiveStatus status, String name);

    List<RoleRes> getRoles(int page, Role role, ActiveStatus status, String name);

    boolean existPermission(int roleId, PermissionType[] groups, PermissionKey key, RoleType roleType);

    Role getRoleForAdmin(int roleId, Role role);

    List<PermissionRes> getPermissions(int roleId, Role role);

    RoleDetail getRoleById(Integer roleId);
}
