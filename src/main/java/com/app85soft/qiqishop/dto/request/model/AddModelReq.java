package com.app85soft.qiqishop.dto.request.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AddModelReq {
    @NotBlank
    String name;
    @NotNull
    Integer productId;
    Integer coverImage;
    boolean hasDiscount;
    int discountPercentage;
    @NotNull
    float price;
    int stock;
}
