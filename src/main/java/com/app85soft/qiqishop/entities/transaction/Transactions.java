package com.app85soft.qiqishop.entities.transaction;

import com.app85soft.qiqishop.dto.constant.PaymentGateway;
import com.app85soft.qiqishop.dto.constant.PaymentMethod;
import com.app85soft.qiqishop.dto.constant.TransactionStatus;
import com.app85soft.qiqishop.entities.BaseEntity;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.Column;
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
@Table(name = "transactions")
public class Transactions extends BaseEntity {

    String code;
    String referenceCode;
    Integer userId;
    Integer orderId;
    BigDecimal amount;
    PaymentGateway paymentGateway;
    @Column(name = "pay_date")
    Long payDate;
    String description;
    PaymentMethod paymentMethod;
    TransactionStatus status;

    boolean deleted;
}
