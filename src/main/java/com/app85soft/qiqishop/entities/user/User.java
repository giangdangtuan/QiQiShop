package com.app85soft.qiqishop.entities.user;

import com.app85soft.qiqishop.dto.constant.ActiveStatus;
import com.app85soft.qiqishop.entities.BaseEntity;
import com.app85soft.qiqishop.entities.role.Role;
import com.app85soft.qiqishop.entities.user.constant.Gender;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.sql.Date;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Table(name = "users")
public class User extends BaseEntity {
    String code;
    String phone;
    String email;
    String name;
    @JsonIgnore
    String password;
    Date birthday;

    @Column(name = "gender", columnDefinition = "INT")
    Gender gender;
    Integer roleId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "roleId", nullable = false, insertable = false, updatable = false)
    Role role;

    @Column(name = "status", columnDefinition = "INT")
    ActiveStatus status;

    boolean deleted;
}
