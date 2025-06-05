package com.app85soft.qiqishop.entities.model;

import com.app85soft.qiqishop.entities.BaseEntity;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Table(name = "model")
public class Model extends BaseEntity {
    String code;
    String name;
    Integer productId;
    BigDecimal price;
    int stock;
    int soldCount;

    boolean deleted;

    @Column(name = "option_value_1_id")
    Integer optionValue1Id;
    @Column(name = "option_value_2_id")
    Integer optionValue2Id;
}
