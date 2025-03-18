package com.app85soft.qiqishop.controller;

import com.app85soft.qiqishop.dto.constant.ActiveStatus;
import com.app85soft.qiqishop.dto.request.IdsRequest;
import com.app85soft.qiqishop.dto.request.role.AddRoleReq;
import com.app85soft.qiqishop.dto.request.role.UpdateRoleReq;
import com.app85soft.qiqishop.dto.response.BaseResponse;
import com.app85soft.qiqishop.dto.response.permission.PermissionRes;
import com.app85soft.qiqishop.dto.response.role.RoleRes;
import com.app85soft.qiqishop.services.role.RoleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/")
@RequiredArgsConstructor
public class RoleController {

    private final RoleService roleService;

    @Operation(summary = "Add role")
    @PostMapping("v1/role/add")
    public ResponseEntity<BaseResponse<?>> addRole(@RequestBody @Valid AddRoleReq req) {
        return ResponseEntity.ok(new BaseResponse<>(roleService.addRole(req)));
    }

    @Operation(summary = "Update role")
    @PostMapping("v1/role/update")
    public ResponseEntity<BaseResponse<?>> updateRole(@RequestBody @Valid UpdateRoleReq req) {
        return ResponseEntity.ok(new BaseResponse<>(roleService.updateRole(req)));
    }

    @Operation(summary = "Delete roles")
    @PostMapping("v1/role/delete")
    public ResponseEntity<BaseResponse<?>> deleteRoles(@RequestBody @Valid IdsRequest req) {
        return ResponseEntity.ok(new BaseResponse<>(roleService.deleteRoles(req)));
    }

    @Operation(summary = "Get role")
    @GetMapping("v1/role/list")
    public ResponseEntity<BaseResponse<List<RoleRes>>> getRoleList(@RequestParam int page,
                                                                   @RequestParam(required = false) ActiveStatus status,
                                                                   @Parameter(description = "[name]")
                                                                   @RequestParam(required = false) String searchKeyword) {
        return ResponseEntity.ok(roleService.getRoles(status, searchKeyword, page));
    }

    @Operation(summary = "Get detail role")
    @GetMapping("v1/role/detail/{id}")
    public ResponseEntity<BaseResponse<RoleRes>> getDetailRole(@PathVariable(name = "id") int roleId) {
        return ResponseEntity.ok(new BaseResponse<>(roleService.getDetailRole(roleId)));
    }

    @Operation(summary = "Get permissions")
    @GetMapping("v1/role/permissions")
    public ResponseEntity<BaseResponse<List<PermissionRes>>> getPermissions() {
        return ResponseEntity.ok(new BaseResponse<>(roleService.getPermissions()));
    }
}
