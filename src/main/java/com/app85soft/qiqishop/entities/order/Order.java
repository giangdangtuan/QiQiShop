package com.app85soft.qiqishop.entities.order;

import com.app85soft.qiqishop.dto.constant.OrderStatus;
import com.app85soft.qiqishop.dto.constant.PaymentMethod;
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
@Table(name = "orders")
public class Order extends BaseEntity {
    int userId;
    int addressId;
    String code;
    BigDecimal totalPrice;
    PaymentMethod paymentMethod;
    BigDecimal shippingCost;
    String note;
    OrderStatus status;
    boolean rated;

    boolean deleted;
}
