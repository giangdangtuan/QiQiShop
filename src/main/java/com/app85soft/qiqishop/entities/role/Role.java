package com.app85soft.qiqishop.entities.role;

import com.app85soft.qiqishop.dto.constant.ActiveStatus;
import com.app85soft.qiqishop.dto.constant.RoleType;
import com.app85soft.qiqishop.entities.BaseEntity;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "roles")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Role extends BaseEntity {
    int objectId;
    String name;
    String note;

    @Column(name = "type", columnDefinition = "INT")
    RoleType type;
    @Column(name = "status", columnDefinition = "INT")
    ActiveStatus status;

}
