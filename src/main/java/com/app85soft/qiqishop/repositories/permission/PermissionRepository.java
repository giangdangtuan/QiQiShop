package com.app85soft.qiqishop.repositories.permission;

import com.app85soft.qiqishop.dto.constant.RoleType;
import com.app85soft.qiqishop.entities.role.permisstion.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PermissionRepository extends JpaRepository<Permission, Integer> {
    @Query("select p from Permission p where p.status = 1 and p.deleted = false and p.type = :type")
    List<Permission> getPermissions(@Param("type") RoleType type);
}
