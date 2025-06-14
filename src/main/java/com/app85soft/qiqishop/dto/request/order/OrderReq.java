package com.app85soft.qiqishop.dto.request.order;

import com.app85soft.qiqishop.dto.constant.PaymentMethod;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderReq {
    int userId;
    int addressId;
    String code;
    BigDecimal totalPrice;
    PaymentMethod paymentMethod;
    BigDecimal shippingCost;
    String note;
    int status;
}
