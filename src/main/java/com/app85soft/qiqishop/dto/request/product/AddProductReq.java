package com.app85soft.qiqishop.dto.request.product;

import com.app85soft.qiqishop.dto.constant.ActiveStatus;
import com.fasterxml.jackson.annotation.JsonProperty;
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
    @NotBlank
    String description;
    BigDecimal price;
    int stock;
    ActiveStatus status;

    List<Model> models;

    @Data
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class Model {
        String code;
        String name;
        Integer productId;
        Integer coverImage;

        boolean hasDiscount;
        int discountPercentage;
        BigDecimal price;
        int stock;
    }
}
