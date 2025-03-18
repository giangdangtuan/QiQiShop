package com.app85soft.qiqishop.dto.constant;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Kiểu sắp xếp")
public enum SortType implements BaseEnum<String> {
    @Schema(description = "Tăng dần")
    ASC,
    @Schema(description = "Giảm dần")
    DESC;

    @JsonCreator
    public static SortType fromValue(String value) {
        for (SortType column : values()) {
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
