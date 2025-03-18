package com.app85soft.qiqishop.dto.constant;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Trạng thái sử dụng:  0 - INACTIVE, 1 - ACTIVE", type = "integer")
public enum ActiveStatus implements BaseEnum<Integer> {
    INACTIVE(0),
    ACTIVE(1);

    final int value;

    ActiveStatus(int value) {
        this.value = value;
    }

    @JsonCreator
    public static ActiveStatus fromValue(int value) {
        for (ActiveStatus column : values()) {
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
