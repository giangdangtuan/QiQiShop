package com.app85soft.qiqishop.dto.response.cart;

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
public class CartRes {
    int userId;
    List<CartItemRes> items;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class CartItemRes {
        int id;
        int productId;
        String productName;
        int modelId;
        String modelName;
        Integer cover_image;
        int quantity;
        int stock;
        int weight;
        BigDecimal originalPrice;
        BigDecimal finalPrice;
        BigDecimal totalAmount;
    }
}
