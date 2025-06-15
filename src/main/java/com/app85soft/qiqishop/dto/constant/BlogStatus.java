package com.app85soft.qiqishop.dto.constant;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Trạng thái blog0:  0 - DRAFT, 1 - PUBLISHED, 2 - HIDDEN", type = "integer")
public enum BlogStatus implements BaseEnum<Integer> {
    DRAFT(0),
    PUBLISHED(1),
    HIDDEN(2);

    final int value;

    BlogStatus(int value) {
        this.value = value;
    }

    @JsonCreator
    public static BlogStatus fromValue(int value) {
        for (BlogStatus column : values()) {
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
