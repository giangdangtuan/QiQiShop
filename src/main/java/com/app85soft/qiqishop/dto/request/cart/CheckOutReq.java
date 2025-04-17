package com.app85soft.qiqishop.dto.request.cart;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CheckOutReq {
    List<Integer> selectCartItemId;
    Integer addressId;
}
