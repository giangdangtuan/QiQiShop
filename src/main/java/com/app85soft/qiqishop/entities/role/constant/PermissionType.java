package com.app85soft.qiqishop.entities.role.constant;

import com.app85soft.qiqishop.dto.constant.BaseEnum;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum PermissionType implements BaseEnum<String> {

    DASHBOARD,
    ACCOUNT,
    ROLE,

    PRODUCT,
    POST,
    TRANSACTION
    ;


    @JsonCreator
    public static PermissionType fromValue(String value) {
        for (PermissionType column : values()) {
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
