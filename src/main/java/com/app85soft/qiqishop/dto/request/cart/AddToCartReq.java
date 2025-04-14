package com.app85soft.qiqishop.dto.request.cart;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AddToCartReq {
    @NotNull
    Integer modelId;
    @NotNull
    Integer quantity;
}
