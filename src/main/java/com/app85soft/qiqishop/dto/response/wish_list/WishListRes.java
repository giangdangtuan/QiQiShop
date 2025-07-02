package com.app85soft.qiqishop.dto.response.wish_list;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class WishListRes {
    int id;
    int userId;
    int productId;
    String productName;
    String originUrl;
    String thumbUrl;
    String categoryName;

    Date createdAt;
}
