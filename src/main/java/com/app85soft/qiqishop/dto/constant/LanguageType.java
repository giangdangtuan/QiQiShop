package com.app85soft.qiqishop.dto.constant;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Ngôn ngữ sử dụng: 0 - vi, 1 - en, 2 - de")
public enum LanguageType implements BaseEnum<String> {
    vi,
    en,
    de;

    @JsonCreator
    public static LanguageType fromValue(String value) {
        for (LanguageType column : values()) {
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
