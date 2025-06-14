package com.app85soft.qiqishop.repositories.purchase_order_item;

import com.app85soft.qiqishop.dto.response.purchase_order.PurchaseOrderItemRes;

import java.util.List;

public interface PurchaseOrderItemRepositoryCustom {
    List<PurchaseOrderItemRes> getPurchaseOrderItems(int purchaseOrderId);
}
