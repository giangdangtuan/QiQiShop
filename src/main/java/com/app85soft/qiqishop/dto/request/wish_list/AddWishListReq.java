package com.app85soft.qiqishop.dto.request.wish_list;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AddWishListReq {
    int productId;
}
