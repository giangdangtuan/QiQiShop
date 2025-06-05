package com.app85soft.qiqishop.dto.response.product;

import com.app85soft.qiqishop.dto.constant.ActiveStatus;
import com.app85soft.qiqishop.dto.request.model.VariantOptionReq;
import com.app85soft.qiqishop.dto.response.model.ModelRes;
import com.app85soft.qiqishop.dto.response.model.VariantOptionRes;
import com.app85soft.qiqishop.dto.response.rating.RatingRes;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProductDetailRes {
    int id;
    String code;
    String name;
    Integer categoryId;
    Integer coverImage;
    String imageUrl;
    Integer weight;
    String description;
    ActiveStatus status;

    List<VariantOptionRes> variantOptions;
    List<ModelRes> models;
    List<ProductImageRes> productImages;
}
