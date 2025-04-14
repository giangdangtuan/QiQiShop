package com.app85soft.qiqishop.entities.cart;

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
@JsonInclude(JsonInclude.Include.NON_NULL)
@Table(name = "cart_item")
public class CartItem extends BaseEntity {
    @Column(name = "cart_id")
    int cartId;
    @Column(name = "model_id")
    int modelId;
    @Column(name = "quantity")
    int quantity;
}
