package com.app85soft.qiqishop.dto.response.order;

import com.app85soft.qiqishop.dto.constant.OrderStatus;
import com.app85soft.qiqishop.dto.constant.PaymentMethod;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrderListRes {
    int id;
    String code;
    int userId;
    String userName;
    BigDecimal totalPrice;
    PaymentMethod paymentMethod;
    OrderStatus status;

    List<OrderDertailRes> orderDetails;
}
