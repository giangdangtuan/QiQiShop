package com.app85soft.qiqishop.dto.response.address;

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
    int provinceId;
    String provinceName;
    int provinceGhnId;
    int districtId;
    String districtName;
    int districtGhnId;
    int wardId;
    String wardName;
    String wardGhnCode;
    String detailAddress;
    boolean isDefault;

    public AddressRes(int id, int userId, String consignee, String phone,
                      String provinceName, String districtName, String wardName,
                      String detailAddress, boolean isDefault) {
        this.id = id;
        this.userId = userId;
        this.consignee = consignee;
        this.phone = phone;
        this.provinceName = provinceName;
        this.districtName = districtName;
        this.wardName = wardName;
        this.detailAddress = detailAddress;
        this.isDefault = isDefault;
    }

}
