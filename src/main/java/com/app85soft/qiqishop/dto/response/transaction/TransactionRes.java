package com.app85soft.qiqishop.dto.response.transaction;

import com.app85soft.qiqishop.dto.constant.PaymentGateway;
import com.app85soft.qiqishop.dto.constant.PaymentMethod;
import com.app85soft.qiqishop.dto.constant.TransactionStatus;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.Column;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TransactionRes {
    int id;
    String code;
    String referenceCode;
    Integer userId;
    String userName;
    Integer orderId;
    String orderCode;
    BigDecimal amount;
    PaymentGateway paymentGateway;
    @Column(name = "pay_date")
    String description;
    PaymentMethod paymentMethod;
    TransactionStatus status;

    Date createAt;
}
