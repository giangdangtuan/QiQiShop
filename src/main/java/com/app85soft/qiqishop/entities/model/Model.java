package com.app85soft.qiqishop.entities.model;

import com.app85soft.qiqishop.entities.BaseEntity;
import com.app85soft.qiqishop.entities.product.Product;
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
    Integer coverImage;
    BigDecimal price;
    int stock;
    int soldCount;

    boolean deleted;
}
