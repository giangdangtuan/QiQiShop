package com.app85soft.qiqishop.entities.product;

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
@Table(name = "product")
public class Product extends BaseEntity {
    String code;
    String name;
    Integer categoryId;
    Integer coverImage;

    @Column(columnDefinition = "TEXT")
    String description;

    @Column(name = "status", columnDefinition = "INT")
    ActiveStatus status;

    boolean deleted;
}
