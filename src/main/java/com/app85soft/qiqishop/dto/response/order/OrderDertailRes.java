package com.app85soft.qiqishop.dto.response.order;

import com.app85soft.qiqishop.dto.response.model.ModelRes;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrderDertailRes {
    int id;
    int modelId;
    String modelName;
    String productName;
    Integer productImage;
    int amount;
    BigDecimal originalPrice;
    BigDecimal finalPrice;

    boolean rated;
}
