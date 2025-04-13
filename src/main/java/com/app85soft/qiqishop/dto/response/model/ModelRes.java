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
    BigDecimal originalPrice;
    BigDecimal finalPrice;
    int stock;
    int soldCount;

    PromotionInfo promotion;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class PromotionInfo {
        Integer id;
        String name;
        Long startTime;
        Long endTime;
        int discountPercentage;
    }
}
