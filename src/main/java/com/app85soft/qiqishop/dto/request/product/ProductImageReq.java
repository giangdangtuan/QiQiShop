package com.app85soft.qiqishop.dto.request.product;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductImageReq {
    Integer productId;
    Integer imageId;
    Integer sortOrder;
}
