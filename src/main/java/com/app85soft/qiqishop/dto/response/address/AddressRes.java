package com.app85soft.qiqishop.dto.response.address;

import com.app85soft.qiqishop.entities.address.Address;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AddressRes {
    int id;
    int userId;
    String consignee;
    String phone;
    String provinceName;
    int provinceGhnId;
    String districtName;
    int districtGhnId;
    String wardName;
    String wardGhnCode;
    String detailAddress;
    boolean isDefault;
}
