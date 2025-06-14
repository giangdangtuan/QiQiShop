package com.app85soft.qiqishop.repositories.purchase_order;

import com.app85soft.qiqishop.dto.response.purchase_order.PurchaseOrderRes;

import java.util.Date;
import java.util.List;

public interface PurchaseOrderRepositoryCustom {

    long countPurchaseOrder(String searchKeyword, Date startTime, Date endTime);

    List<PurchaseOrderRes> getPurchaseOrders(String searchKeyword, Date startTime, Date endTime, int page);

    PurchaseOrderRes getPurchaseOrder(int purchaseOrderId);
}
