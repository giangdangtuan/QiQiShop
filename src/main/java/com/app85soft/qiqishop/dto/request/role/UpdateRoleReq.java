package com.app85soft.qiqishop.dto.request.role;

import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.FieldDefaults;

@EqualsAndHashCode(callSuper = true)
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpdateRoleReq extends AddRoleReq {
    @NotNull(message = "ID không được để trống.")
    Integer id;

}
