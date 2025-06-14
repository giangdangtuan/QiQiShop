package com.app85soft.qiqishop.repositories.purchase_order_item;

import com.app85soft.qiqishop.dto.response.purchase_order.PurchaseOrderItemRes;
import com.app85soft.qiqishop.entities.model.QModel;
import com.app85soft.qiqishop.entities.product.QProduct;
import com.app85soft.qiqishop.entities.purchase_orders.QPurchaseOrderItem;
import com.app85soft.qiqishop.entities.upload_file.QUploadFile;
import com.app85soft.qiqishop.repositories.BaseRepository;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;

@Slf4j
@Repository
public class PurchaseOrderItemRepositoryImpl extends BaseRepository implements PurchaseOrderItemRepositoryCustom {
    private final QPurchaseOrderItem qPurchaseOrderItem = QPurchaseOrderItem.purchaseOrderItem;
    private final QModel qModel = QModel.model;
    private final QProduct qProduct = QProduct.product;
    private final QUploadFile qUploadFile = QUploadFile.uploadFile;

    @Override
    public List<PurchaseOrderItemRes> getPurchaseOrderItems(int purchaseOrderId) {
        BooleanBuilder builder = new BooleanBuilder();
        builder.and(qPurchaseOrderItem.deleted.eq(false));
        builder.and(qPurchaseOrderItem.purchaseOrderId.eq(purchaseOrderId));

        return query().from(qPurchaseOrderItem)
                .leftJoin(qModel).on(qPurchaseOrderItem.purchaseOrderId.eq(qModel.id))
                .leftJoin(qProduct).on(qProduct.id.eq(qModel.productId))
                .leftJoin(qUploadFile).on(qUploadFile.id.eq(qProduct.coverImage))
                .where(builder)
                .select(Projections.fields(PurchaseOrderItemRes.class,
                        qPurchaseOrderItem.purchaseOrderId.as("purchaseOrderId"),
                        qProduct.id.as("productId"),
                        qProduct.name.as("productName"),
                        qPurchaseOrderItem.modelId.as("modelId"),
                        qModel.name.as("modelName"),
                        qPurchaseOrderItem.quantity.as("quantity"),
                        qPurchaseOrderItem.unitCost.as("unitCost"),
                        qUploadFile.originUrl.as("originUrl"),
                        qUploadFile.thumbUrl.as("thumbUrl")
                ))
                .fetch();
    }
}
