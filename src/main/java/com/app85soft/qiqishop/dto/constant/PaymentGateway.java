package com.app85soft.qiqishop.dto.constant;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Loại thanh toán:  0 - CASH, 1 - VNPAY, 2 - MOMO", type = "integer")
public enum PaymentGateway implements BaseEnum<Integer> {
    CASH(0),
    VNPAY(1),
    MOMO(2);

    final int value;

    PaymentGateway(int value) {
        this.value = value;
    }

    @JsonCreator
    public static PaymentGateway fromValue(int value) {
        for (PaymentGateway column : values()) {
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
