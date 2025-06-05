package com.app85soft.qiqishop.dto.request.payment;

import com.app85soft.qiqishop.dto.constant.PaymentGateway;
import com.app85soft.qiqishop.dto.constant.PaymentMethod;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ConfirmCheckOutReq {
    int addressId;
    List<Integer> selectCartItemId;
    BigDecimal totalAmount;
    PaymentMethod paymentMethod;
    PaymentGateway paymentGateway;
}
