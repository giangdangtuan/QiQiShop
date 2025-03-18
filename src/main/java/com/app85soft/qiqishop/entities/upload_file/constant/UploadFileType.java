package com.app85soft.qiqishop.entities.upload_file.constant;

import com.app85soft.qiqishop.dto.constant.BaseEnum;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Kiểu file upload: 0 - IMAGE, 1 - VIDEO_YOUTUBE, 2 - PDF", type = "integer")
public enum UploadFileType implements BaseEnum<Integer> {
    IMAGE(0),
    VIDEO_YOUTUBE(1),
    PDF(2);

    final int value;

    UploadFileType(int value) {
        this.value = value;
    }

    @JsonCreator
    public static UploadFileType fromValue(int value) {
        for (UploadFileType column : values()) {
            if (column.value == value) {
                return column;
            }
        }
        return null;
    }

    @JsonValue
    public Integer toValue() {
        return value;
    }

}
