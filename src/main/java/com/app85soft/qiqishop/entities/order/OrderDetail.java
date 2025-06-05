package com.app85soft.qiqishop.entities.order;

import com.app85soft.qiqishop.entities.BaseEntity;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
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
@Table(name = "order_detail")
public class OrderDetail extends BaseEntity {
    int orderId;
    int modelId;
    int amount;
    BigDecimal originalPrice;
    BigDecimal finalPrice;

    boolean deleted;
}
