package com.app85soft.qiqishop.entities;

import com.app85soft.qiqishop.dto.constant.ActiveStatus;
import com.app85soft.qiqishop.dto.request.user.RegisterUser;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@MappedSuperclass
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public abstract class BusinessEntity extends BaseEntity {
    String code;
    String name;
    String phone;
    String email;
    String address;
    String taxCode;

    @Column(columnDefinition = "TEXT")
    String description;

    @Column(name = "status", columnDefinition = "INT")
    ActiveStatus status;

    boolean deleted;

    public BusinessEntity(RegisterUser registerUser, String code) {
        this.setCode(code);
        this.setName(registerUser.getBusinessName());
        this.setAddress(registerUser.getBusinessAddress());
        this.setTaxCode(registerUser.getBusinessTax());
        this.setEmail(registerUser.getBusinessEmail());
        this.setPhone(registerUser.getBusinessPhone());
        this.setStatus(ActiveStatus.ACTIVE);
    }

}
