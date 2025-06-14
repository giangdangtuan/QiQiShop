package com.app85soft.qiqishop.dto.response.purchase_order;

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
public class PurchaseOrderItemRes {
    int purchaseOrderId;
    int productId;
    String productName;
    int modelId;
    String modelName;
    Integer quantity;
    BigDecimal unitCost;
    String originUrl;
    String thumbUrl;
}
