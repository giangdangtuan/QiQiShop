package com.app85soft.qiqishop.dto.constant;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Loại thanh toán:  0 - PENDING, 1 - SUCCESS, 2 - FAILED, 3 - CANCELLED", type = "integer")
public enum TransactionStatus implements BaseEnum<Integer> {
    PENDING(0),
    SUCCESS(1),
    FAILED(2),
    CANCELLED(3);

    final int value;

    TransactionStatus(int value) {
        this.value = value;
    }

    @JsonCreator
    public static TransactionStatus fromValue(int value) {
        for (TransactionStatus column : values()) {
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
