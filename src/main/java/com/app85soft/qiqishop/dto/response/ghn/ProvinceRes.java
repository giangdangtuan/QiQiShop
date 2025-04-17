package com.app85soft.qiqishop.dto.response.ghn;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProvinceRes {
    @JsonProperty("ProvinceID")
    int provinceId;

    @JsonProperty("Code")
    String provinceCode;

    @JsonProperty("ProvinceName")
    String provinceName;
}

