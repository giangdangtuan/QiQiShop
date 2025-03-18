package com.app85soft.qiqishop.services.role;

import com.app85soft.qiqishop.dto.constant.ActiveStatus;
import com.app85soft.qiqishop.dto.request.IdsRequest;
import com.app85soft.qiqishop.dto.request.role.AddRoleReq;
import com.app85soft.qiqishop.dto.request.role.UpdateRoleReq;
import com.app85soft.qiqishop.dto.response.BaseResponse;
import com.app85soft.qiqishop.dto.response.permission.PermissionRes;
import com.app85soft.qiqishop.dto.response.role.RoleRes;
import com.app85soft.qiqishop.entities.role.Role;

import java.util.List;

public interface RoleService {
    Role addRole(AddRoleReq role);
    Role updateRole(UpdateRoleReq roleReq);
    List<Integer> deleteRoles(IdsRequest req);
    BaseResponse<List<RoleRes>> getRoles(ActiveStatus status, String name, int page);
    RoleRes getDetailRole(int roleId);
    List<PermissionRes> getPermissions();
}
