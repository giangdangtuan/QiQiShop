package com.app85soft.qiqishop.entities.role.constant;

import com.app85soft.qiqishop.dto.constant.BaseEnum;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum PermissionGroup implements BaseEnum<String> {
    STATISTIC,
    CONFIG,
    FEATURE;


    @JsonCreator
    public static PermissionGroup fromValue(String value) {
        for (PermissionGroup column : values()) {
            if (column.toValue().equals(value)) {
                return column;
            }
        }
        return null;
    }

    @Override
    @JsonValue
    public String toValue() {
        return name();
    }
}
