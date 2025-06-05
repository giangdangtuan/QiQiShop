package com.app85soft.qiqishop.dto.constant;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Trạng thái đơn hàng", type = "integer")
public enum OrderStatus implements BaseEnum<Integer> {
    WAITING_FOR_CONFIMATION(0),
    READY_TO_PICK(1),
    PICKING(2),
    CANCEL(3),
    MONEY_COLLECT_PICKING(4),
    PICKED(5),
    STORING(6),
    TRANSPORTING(7),
    SORTING(8),
    DELIVERING(9),
    MONEY_COLLECT_DELIVERING(10),
    DELIVERED(11),
    DELIVERY_FAIL(12),
    WAITING_TO_RETURN(13),
    RETURN_TRANSPORTING(14),
    RETURN_SORTING(15),
    RETURNING(16),
    RETURN_FAIL(17),
    RETURNED(18),
    EXCEPTION(19),
    DAMAGE(20),
    LOST(21);

    final int value;

    OrderStatus(int value) {
        this.value = value;
    }

    @JsonCreator
    public static OrderStatus fromValue(int value) {
        for (OrderStatus column : values()) {
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
