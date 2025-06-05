package com.app85soft.qiqishop.dto.request.role;

import com.app85soft.qiqishop.dto.constant.ActiveStatus;
import com.app85soft.qiqishop.dto.constant.RoleType;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AddRoleReq {
    @NotBlank(message = "Tên vai trò không được để trống")
    String name;
    String note;
//    @NotNull
    RoleType type;
    @NotNull
    ActiveStatus status;

    @NotNull
    @Size(min = 1)
    List<Permission> permissions;

    @Data
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class Permission {
        @NotNull
        Integer id;
        @JsonProperty("isView")
        Boolean isView;
        @JsonProperty("isWrite")
        Boolean isWrite;
        @JsonProperty("isApproval")
        Boolean isApproval;
        @JsonProperty("isDecision")
        Boolean isDecision;
    }
}
