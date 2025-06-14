package com.app85soft.qiqishop.services.purchase_order;

import com.app85soft.qiqishop.dto.request.purchase_order.AddPurchaseOrderReq;
import com.app85soft.qiqishop.dto.response.BaseResponse;
import com.app85soft.qiqishop.dto.response.purchase_order.PurchaseOrderRes;

import java.util.Date;
import java.util.List;

public interface PurchaseOrderService {
    PurchaseOrderRes addPurchaseOrder(AddPurchaseOrderReq req);
    BaseResponse<List<PurchaseOrderRes>> getPurchaseOrders(String code, Date startTime, Date endTime, int page);
    BaseResponse<PurchaseOrderRes> getPurchaseOrder(int purchaseOrderId);
}
