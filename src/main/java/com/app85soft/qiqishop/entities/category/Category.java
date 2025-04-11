package com.app85soft.qiqishop.entities.category;

import java.sql.Date;

import com.app85soft.qiqishop.dto.constant.ActiveStatus;
import com.app85soft.qiqishop.entities.BaseEntity;
import com.app85soft.qiqishop.entities.role.Role;
import com.app85soft.qiqishop.entities.user.constant.Gender;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Table(name = "category")
public class Category extends BaseEntity {
    String name;
    @Column(name = "status", columnDefinition = "INT")
    ActiveStatus status;
    boolean deleted;
}
