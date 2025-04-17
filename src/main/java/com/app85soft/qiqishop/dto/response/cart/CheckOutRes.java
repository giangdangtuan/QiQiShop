package com.app85soft.qiqishop.dto.response.cart;

import com.app85soft.qiqishop.dto.response.address.AddressRes;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CheckOutRes {
    List<CartRes.CartItemRes> items;
    AddressRes address;
    BigDecimal merchandiseTotal;
    BigDecimal shippingCost;
    BigDecimal totalAmount;

}
