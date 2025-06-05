package com.app85soft.qiqishop.dto.request.ghn;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class GhnCreateOrderReq {
    @JsonProperty("payment_type_id")
    int paymentTypeId;
    @JsonProperty("required_note")
    String requiredNote;
    @JsonProperty("to_name")
    String toName;
    @JsonProperty("to_phone")
    String toPhone;
    @JsonProperty("to_address")
    String toAddress;
    @JsonProperty("to_ward_name")
    String toWardName;
    @JsonProperty("to_district_name")
    String toDistrictName;
    @JsonProperty("to_province_name")
    String toProvinceName;
    @JsonProperty("cod_amount")
    Integer codAmount;
    int weight;
    @JsonProperty("service_type_id")
    int serviceTypeId;
    List<GhnItemReq> items;
}
