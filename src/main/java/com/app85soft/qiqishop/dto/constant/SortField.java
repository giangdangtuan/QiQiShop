package com.app85soft.qiqishop.dto.constant;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Trường sắp xếp: 0- ID, 1 - thơi gian update")
public enum SortField implements BaseEnum<String> {
    ID,
    UPDATE_AT;

    @JsonCreator
    public static SortField fromValue(String value) {
        for (SortField column : values()) {
            if (column.toValue().equalsIgnoreCase(value)) {
                return column;
            }
        }
        return null;
    }

    @JsonValue
    public String toValue() {
        return toString();
    }

}
