package com.app85soft.qiqishop.entities.role.permisstion;

import com.app85soft.qiqishop.dto.constant.ActiveStatus;
import com.app85soft.qiqishop.dto.constant.RoleType;
import com.app85soft.qiqishop.entities.BaseEntity;
import com.app85soft.qiqishop.entities.role.constant.PermissionGroup;
import com.app85soft.qiqishop.entities.role.constant.PermissionType;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "permissions")
public class Permission extends BaseEntity {
    String title;
    @Enumerated(value = EnumType.STRING)
    PermissionType permission;
    @Column(name = "parent_permission")
    @Enumerated(value = EnumType.STRING)
    PermissionGroup parentPermission;
    @Column(name = "can_view")
    @JsonProperty("view")
    Boolean isView;
    @Column(name = "can_write")
    @JsonProperty("write")
    Boolean isWrite;
    @Column(name = "can_approval")
    @JsonProperty("approval")
    Boolean isApproval;
    @Column(name = "can_decision")
    @JsonProperty("decision")
    Boolean isDecision;

    @Column(name = "type", columnDefinition = "INT")
    RoleType type;
    @Column(name="status", columnDefinition = "INT")
    ActiveStatus status;
}
