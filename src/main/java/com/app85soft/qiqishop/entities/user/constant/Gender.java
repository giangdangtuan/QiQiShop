package com.app85soft.qiqishop.entities.user.constant;

import com.app85soft.qiqishop.dto.constant.BaseEnum;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Giới tính: 0 - Khac, 1 - Nam, 2 - Nu", type = "integer")
public enum Gender implements BaseEnum<Integer> {
    OTHER,
    MALE,
    FEMALE;

    @JsonCreator
    public static Gender fromValue(int value){
        for (Gender column: values()){
            if(column.toValue()==value)
                return column;
        }
        return null;
    }

    @JsonValue
    public Integer toValue() {
        return ordinal();
    }
}
