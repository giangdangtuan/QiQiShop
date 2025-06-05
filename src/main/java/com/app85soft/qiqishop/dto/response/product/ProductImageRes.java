package com.app85soft.qiqishop.dto.response.product;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProductImageRes {
    int id;
    Integer productId;
    Integer imageId;
    String imageUrl;
    Integer sortOrder;
}
