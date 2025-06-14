package com.app85soft.qiqishop.dto.response.order;

import com.app85soft.qiqishop.dto.constant.OrderStatus;
import com.app85soft.qiqishop.dto.constant.PaymentGateway;
import com.app85soft.qiqishop.dto.constant.PaymentMethod;
import com.app85soft.qiqishop.dto.response.address.AddressRes;
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
public class OrderRes {
    int id;
    String code;
    int userId;
    String userName;
    AddressRes address;
    BigDecimal totalPrice;
    PaymentMethod paymentMethod;
    BigDecimal shippingCost;
    String note;

    public OrderRes(int id, String code, int userId, String userName, BigDecimal totalPrice,
                    PaymentMethod paymentMethod, BigDecimal shippingCost, String note,
                    OrderStatus status, AddressRes address) {
        this.id = id;
        this.code = code;
        this.userId = userId;
        this.userName = userName;
        this.totalPrice = totalPrice;
        this.paymentMethod = paymentMethod;
        this.shippingCost = shippingCost;
        this.note = note;
        this.status = status;
        this.address = address;
    }

    OrderStatus status;

    List<OrderDertailRes> orderDetails;
}
