package com.app85soft.qiqishop.dto.request.purchase_order;

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
public class AddPurchaseOrderReq {
    String note;

    List<PurchaseOrderItem> purchaseOrderItems;

    @Data
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class PurchaseOrderItem {
        int modelId;
        Integer quantity;
        BigDecimal unitCost;
    }
}
