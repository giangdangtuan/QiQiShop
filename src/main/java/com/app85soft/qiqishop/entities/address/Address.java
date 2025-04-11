package com.app85soft.qiqishop.entities.address;

import com.app85soft.qiqishop.entities.BaseEntity;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Table(name = "address")
public class Address extends BaseEntity {
    String consignee;
    String phone;
    boolean type;
    Integer userId;
    boolean deleted;
}
