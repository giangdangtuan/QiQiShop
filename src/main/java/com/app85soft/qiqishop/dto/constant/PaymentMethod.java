package com.app85soft.qiqishop.dto.constant;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Loại thanh toán:  0 - CASH_ON_DELIVERY, 1 - BANK_TRANSFER", type = "integer")
public enum PaymentMethod implements BaseEnum<Integer> {
    CASH_ON_DELIVERY(0),
    BANK_TRANSFER(1);

    final int value;

    PaymentMethod(int value) {
        this.value = value;
    }

    @JsonCreator
    public static PaymentMethod fromValue(int value) {
        for (PaymentMethod column : values()) {
            if (column.toValue() == value) {
                return column;
            }
        }
        return null;
    }

    @JsonValue
    public Integer toValue() {
        return ordinal();
    }
}
