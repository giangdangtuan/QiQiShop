package com.app85soft.qiqishop.repositories.role;

import com.app85soft.qiqishop.entities.role.role_permission.RolePermission;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RolePermissionRepository extends JpaRepository<RolePermission, Integer> {
    RolePermission findByRoleIdAndPermissionId(int roleId, int permissionId);
}
