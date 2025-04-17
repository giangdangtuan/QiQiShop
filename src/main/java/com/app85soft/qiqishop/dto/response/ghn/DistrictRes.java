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
public class DistrictRes {
    @JsonProperty("DistrictID")
    int districtId;

    @JsonProperty("Code")
    String districtCode;

    @JsonProperty("DistrictName")
    String districtName;
}
