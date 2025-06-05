package com.app85soft.qiqishop.dto.request.transaction;

import com.app85soft.qiqishop.dto.constant.PaymentGateway;
import com.app85soft.qiqishop.dto.constant.PaymentMethod;
import com.app85soft.qiqishop.dto.constant.TransactionStatus;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TransactionReq {
    String referenceCode;
    Integer userId;
    Integer orderId;
    BigDecimal amount;
    PaymentGateway paymentGateway;
    Long payDate;
    String description;
    PaymentMethod paymentMethod;
    TransactionStatus status;
}
