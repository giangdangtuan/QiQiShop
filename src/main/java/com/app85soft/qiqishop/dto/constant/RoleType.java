package com.app85soft.qiqishop.dto.constant;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Loại quyền hạn của người dùng: 0 - ADMIN, 1 - USER", type = "integer")
public enum RoleType implements BaseEnum<Integer> {
    ADMIN(0),
    USER(1);

    final int value;

    RoleType(int value) {
        this.value = value;
    }

    @JsonCreator
    public static RoleType fromValue(int value) {
        for (RoleType column : values()) {
            if (column.toValue() == value)
                return column;
        }
        return null;
    }

    @JsonValue
    public Integer toValue() {
        return value;
    }
}

