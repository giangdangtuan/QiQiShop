package com.app85soft.qiqishop.dto.response.model;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ModelRes {
    String code;
    Integer productId;
    String name;
    Integer coverImage;
    boolean hasDiscount;
    int discountPercentage;
    BigDecimal price;
    int stock;
    int soldCount;

    public ModelRes(String code, String name, Integer productId, Integer coverImage, boolean hasDiscount, int discountPercentage, BigDecimal price, int stock) {
        this.code = code;
        this.name = name;
        this.productId = productId;
        this.coverImage = coverImage;
        this.hasDiscount = hasDiscount;
        this.discountPercentage = discountPercentage;
        this.price = price;
        this.stock = stock;
    }
}
