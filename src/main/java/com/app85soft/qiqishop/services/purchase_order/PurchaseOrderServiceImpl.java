package com.app85soft.qiqishop.services.purchase_order;

import com.app85soft.qiqishop.component.Translator;
import com.app85soft.qiqishop.dto.request.purchase_order.AddPurchaseOrderReq;
import com.app85soft.qiqishop.dto.response.BaseResponse;
import com.app85soft.qiqishop.dto.response.purchase_order.PurchaseOrderRes;
import com.app85soft.qiqishop.entities.model.Model;
import com.app85soft.qiqishop.entities.purchase_orders.PurchaseOrder;
import com.app85soft.qiqishop.entities.purchase_orders.PurchaseOrderItem;
import com.app85soft.qiqishop.entities.purchase_orders.StockBatch;
import com.app85soft.qiqishop.entities.role.constant.PermissionKey;
import com.app85soft.qiqishop.entities.role.constant.PermissionType;
import com.app85soft.qiqishop.entities.user.User;
import com.app85soft.qiqishop.exceptions.BusinessException;
import com.app85soft.qiqishop.repositories.model.ModelRepository;
import com.app85soft.qiqishop.repositories.purchase_order.PurchaseOrderRepository;
import com.app85soft.qiqishop.repositories.purchase_order_item.PurchaseOrderItemRepository;
import com.app85soft.qiqishop.repositories.stock_batch.StockBatchRepository;
import com.app85soft.qiqishop.services.BaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PurchaseOrderServiceImpl extends BaseService implements PurchaseOrderService {
    private final PurchaseOrderRepository purchaseOrderRepository;
    private final PurchaseOrderItemRepository purchaseOrderItemRepository;
    private final StockBatchRepository stockBatchRepository;
    private final ModelRepository modelRepository;

    @Override
    public PurchaseOrderRes addPurchaseOrder(AddPurchaseOrderReq req) {
        User user = getUser(PermissionKey.CREATE, PermissionType.PRODUCT);

        String code = "PO" + System.currentTimeMillis();
        PurchaseOrder purchaseOrder = new PurchaseOrder();
        purchaseOrder.setUserId(user.getId());
        purchaseOrder.setNote(req.getNote());
        purchaseOrder.setCode(code);
        purchaseOrderRepository.save(purchaseOrder);

        for(AddPurchaseOrderReq.PurchaseOrderItem item : req.getPurchaseOrderItems()) {
            PurchaseOrderItem purchaseOrderItem = new PurchaseOrderItem();
            purchaseOrderItem.setPurchaseOrderId(purchaseOrder.getId());
            purchaseOrderItem.setQuantity(item.getQuantity());
            purchaseOrderItem.setUnitCost(item.getUnitCost());
            purchaseOrderItem.setModelId(item.getModelId());
            purchaseOrderItemRepository.save(purchaseOrderItem);

            StockBatch stockBatch = new StockBatch();
            stockBatch.setPurchaseOrderItemId(purchaseOrderItem.getId());
            stockBatch.setUnitCost(item.getUnitCost());
            stockBatch.setModelId(item.getModelId());
            stockBatch.setQuantityReceived(item.getQuantity());
            stockBatch.setQuantityRemaining(item.getQuantity());
            stockBatchRepository.save(stockBatch);

            Model model = modelRepository.getById(item.getModelId());
            model.setStock(model.getStock() + item.getQuantity());
            modelRepository.save(model);
        }

        PurchaseOrderRes purchaseOrderRes = getPurchaseOrderRes(purchaseOrder);
        return purchaseOrderRes;
    }

    @Override
    public BaseResponse<List<PurchaseOrderRes>> getPurchaseOrders(String searchKeyword, Date startTime, Date endTime, int page) {
        User user = getUser(PermissionKey.READ, PermissionType.PRODUCT);
        long countPurchaseOrder = purchaseOrderRepository.countPurchaseOrder(searchKeyword, startTime, endTime);
        List<PurchaseOrderRes> listPurchaseOrders = purchaseOrderRepository.getPurchaseOrders(searchKeyword, startTime, endTime, page);
        return new BaseResponse<>(listPurchaseOrders, countPurchaseOrder, page);
    }

    @Override
    public BaseResponse<PurchaseOrderRes> getPurchaseOrder(int purchaseOrderId) {
        User user = getUser(PermissionKey.READ, PermissionType.PRODUCT);

        PurchaseOrderRes purchaseOrderRes = purchaseOrderRepository.getPurchaseOrder(purchaseOrderId);
        if (purchaseOrderRes == null) {
            throw new BusinessException(Translator.toLocale("id_not_exist"), HttpStatus.NOT_FOUND);
        }
        return new BaseResponse<>(purchaseOrderRes);
    }

    private PurchaseOrderRes getPurchaseOrderRes(PurchaseOrder purchaseOrder) {
        return PurchaseOrderRes.builder()
                .id(purchaseOrder.getId())
                .note(purchaseOrder.getNote())
                .code(purchaseOrder.getCode())
                .userId(purchaseOrder.getUserId())
                .items(purchaseOrderItemRepository.getPurchaseOrderItems(purchaseOrder.getId()))
                .build();
    }
}
