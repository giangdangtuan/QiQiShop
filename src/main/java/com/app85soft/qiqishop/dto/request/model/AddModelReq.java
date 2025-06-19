package com.app85soft.qiqishop.dto.request.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AddModelReq {
    Integer id;
    String code;
    @NotBlank
    String name;
    @NotNull
    Integer productId;
    @NotNull
    BigDecimal price;

    Integer optionValue1Id;
    Integer optionValue2Id;
}
