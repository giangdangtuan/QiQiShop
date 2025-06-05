package com.app85soft.qiqishop.dto.request.product;

import com.app85soft.qiqishop.dto.constant.ActiveStatus;
import com.app85soft.qiqishop.dto.request.model.AddModelReq;
import com.app85soft.qiqishop.dto.request.model.VariantOptionReq;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AddProductReq {
    @NotBlank
    String name;
    @NotNull
    Integer categoryId;
    Integer coverImage;
    Integer weight;
    @NotBlank
    String description;
    BigDecimal price;
    int stock;
    ActiveStatus status;

    List<VariantOptionReq> variantOptions;
    List<AddModelReq> models;

    List<ProductImageReq> images;
}
