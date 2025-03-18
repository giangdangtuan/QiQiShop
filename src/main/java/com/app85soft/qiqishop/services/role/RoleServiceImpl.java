package com.app85soft.qiqishop.services.role;

import com.app85soft.qiqishop.component.Translator;
import com.app85soft.qiqishop.dto.constant.ActiveStatus;
import com.app85soft.qiqishop.dto.constant.RoleType;
import com.app85soft.qiqishop.dto.request.IdsRequest;
import com.app85soft.qiqishop.dto.request.role.AddRoleReq;
import com.app85soft.qiqishop.dto.request.role.UpdateRoleReq;
import com.app85soft.qiqishop.dto.response.BaseResponse;
import com.app85soft.qiqishop.dto.response.permission.PermissionRes;
import com.app85soft.qiqishop.dto.response.role.RoleRes;
import com.app85soft.qiqishop.entities.role.Role;
import com.app85soft.qiqishop.entities.role.constant.PermissionKey;
import com.app85soft.qiqishop.entities.role.constant.PermissionType;
import com.app85soft.qiqishop.entities.role.permisstion.Permission;
import com.app85soft.qiqishop.entities.role.role_permission.RolePermission;
import com.app85soft.qiqishop.entities.user.User;
import com.app85soft.qiqishop.exceptions.BusinessException;
import com.app85soft.qiqishop.repositories.permission.PermissionRepository;
import com.app85soft.qiqishop.repositories.role.RolePermissionRepository;
import com.app85soft.qiqishop.repositories.role.RoleRepository;
import com.app85soft.qiqishop.services.BaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;


@Service
@RequiredArgsConstructor
public class RoleServiceImpl extends BaseService implements RoleService {
    private final RoleRepository roleRepository;
    private final RolePermissionRepository rolePermissionRepository;
    private final PermissionRepository permissionRepository;

    @Transactional
    @Override
    public Role addRole(AddRoleReq roleReq) {
        User user = getUser(PermissionKey.CREATE, PermissionType.ROLE);
        Role role = new Role();
        role.setObjectId(user.getRole().getObjectId());
        role.setType(user.getRole().getType());
        role.setName(roleReq.getName());
        role.setNote(roleReq.getNote());
        role.setStatus(roleReq.getStatus());
        roleRepository.save(role);

        List<RolePermission> permissions = new ArrayList<>();

        for (AddRoleReq.Permission item : roleReq.getPermissions()) {
            RolePermission permission = new RolePermission();
            permission.setPermissionId(item.getId());
            permission.setRoleId(role.getId());
            permission.setIsView(item.getIsView());
            permission.setIsApproval(item.getIsApproval());
            permission.setIsWrite(item.getIsWrite());
            permission.setIsDecision(item.getIsDecision());
            permissions.add(permission);
        }
        rolePermissionRepository.saveAll(permissions);

        return role;
    }

    @Transactional
    @Override
    public Role updateRole(UpdateRoleReq roleReq) {
        User user = getUser(PermissionKey.CREATE, PermissionType.ROLE);
        Role role = roleRepository.getRoleForAdmin(roleReq.getId(), user.getRole());
        if (role == null) {
            throw new BusinessException(Translator.toLocale("id_not_exist"), HttpStatus.NOT_FOUND);
        }
        role.setName(roleReq.getName());
        role.setStatus(roleReq.getStatus());
        role.setNote(roleReq.getNote());
        roleRepository.save(role);

        List<RolePermission> permissions = new ArrayList<>();

        for (AddRoleReq.Permission item : roleReq.getPermissions()) {
            RolePermission permission = rolePermissionRepository.findByRoleIdAndPermissionId(role.getId(), item.getId());
            if (permission == null) {
                permission = new RolePermission();
            }
            permission.setPermissionId(item.getId());
            permission.setRoleId(role.getId());
            permission.setIsView(item.getIsView());
            permission.setIsApproval(item.getIsApproval());
            permission.setIsWrite(item.getIsWrite());
            permission.setIsDecision(item.getIsDecision());
            permissions.add(permission);
        }
        rolePermissionRepository.saveAll(permissions);

        return role;
    }

    @Override
    public List<Integer> deleteRoles(IdsRequest req) {
        User user = getUser(PermissionKey.DECISION, PermissionType.ROLE);
        List<Integer> roleIds = req.getIds();
        List<Integer> existingIds = roleRepository.getAllIdToCheckExist(user.getRole(), roleIds);
        List<Integer> nonExistingIds = roleIds.stream().filter(id -> !existingIds.contains(id)).toList();
        if (!nonExistingIds.isEmpty()) {
            throw new BusinessException(Translator.toLocale("id_not_exist"), HttpStatus.BAD_REQUEST);
        }
        roleRepository.deleteRoleByIds(roleIds);
        return roleIds;
    }

    @Override
    public BaseResponse<List<RoleRes>> getRoles(ActiveStatus status, String name, int page) {
        User user = getUser(PermissionKey.READ, PermissionType.ROLE);
        long countRole = roleRepository.countRoles(user.getRole(), status, name);
        List<RoleRes> listRole = roleRepository.getRoles(page, user.getRole(), status, name);
        return new BaseResponse<>(listRole, countRole, page);
    }

    @Override
    public RoleRes getDetailRole(int roleId) {
        User user = getUser(PermissionKey.READ, PermissionType.ROLE);
        Role role = roleRepository.getRoleForAdmin(roleId, user.getRole());
        RoleRes roleRes = RoleRes.convertObject(role);
        roleRes.setPermissions(roleRepository.getPermissions(roleId, user.getRole()));
        return roleRes;
    }

    @Override
    public List<PermissionRes> getPermissions() {
        User user = getUser(PermissionKey.READ, PermissionType.ROLE);
        List<Permission> permissions = permissionRepository.getPermissions(RoleType.ADMIN);
        List<PermissionRes> permissionRes = new ArrayList<>();
        for (Permission item : permissions) {
            PermissionRes permission = new PermissionRes();
            permission.setId(item.getId());
            permission.setPermission(item.getPermission());
            permission.setParentPermission(item.getParentPermission());
            permission.setTitle(item.getTitle());
            permission.setIsView(item.getIsView());
            permission.setIsWrite(item.getIsWrite());
            permission.setIsApproval(item.getIsApproval());
            permission.setIsDecision(item.getIsDecision());
            permissionRes.add(permission);
        }
        return permissionRes;
    }
}
