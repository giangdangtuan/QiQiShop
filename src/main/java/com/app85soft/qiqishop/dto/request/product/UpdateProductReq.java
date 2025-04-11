package com.app85soft.qiqishop.dto.request.product;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

@EqualsAndHashCode(callSuper = true)
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpdateProductReq extends AddProductReq {
    @NotNull(message = "ID không được để trống.")
    Integer id;
}
