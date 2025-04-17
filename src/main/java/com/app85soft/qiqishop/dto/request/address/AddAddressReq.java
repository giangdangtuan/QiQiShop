package com.app85soft.qiqishop.dto.request.address;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AddAddressReq {
    @NotBlank
    String consignee;
    @NotBlank
    String phone;
    @NotNull
    Integer provinceId;
    @NotNull
    Integer districtId;
    @NotNull
    Integer wardId;
    @NotBlank
    String detailAddress;

    boolean isDefault;
}
