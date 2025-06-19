package com.app85soft.qiqishop.repositories.purchase_order;

import com.app85soft.qiqishop.dto.response.promotion.PromotionRes;
import com.app85soft.qiqishop.dto.response.purchase_order.PurchaseOrderItemRes;
import com.app85soft.qiqishop.dto.response.purchase_order.PurchaseOrderRes;
import com.app85soft.qiqishop.entities.model.QModel;
import com.app85soft.qiqishop.entities.product.QProduct;
import com.app85soft.qiqishop.entities.purchase_orders.QPurchaseOrder;
import com.app85soft.qiqishop.entities.purchase_orders.QPurchaseOrderItem;
import com.app85soft.qiqishop.entities.upload_file.QUploadFile;
import com.app85soft.qiqishop.entities.user.QUser;
import com.app85soft.qiqishop.repositories.BaseRepository;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.stream.Collectors;

import static com.app85soft.qiqishop.util.Constants.PAGE_SIZE;

@Slf4j
@Repository
public class PurchaseOrderRepositoryImpl extends BaseRepository implements PurchaseOrderRepositoryCustom {
    private final QPurchaseOrder qPurchaseOrder = QPurchaseOrder.purchaseOrder;
    private final QPurchaseOrderItem qPurchaseOrderItem = QPurchaseOrderItem.purchaseOrderItem;
    private final QUser qUser = QUser.user;
    private final QModel qModel = QModel.model;
    private final QProduct qProduct = QProduct.product;
    private final QUploadFile qUploadFile = QUploadFile.uploadFile;

    @Override
    public long countPurchaseOrder(String searchKeyword, Date startTime, Date endTime) {
        BooleanBuilder builder = new BooleanBuilder();
        builder.and(qPurchaseOrder.deleted.eq(false));
        if (searchKeyword != null) {
            builder.andAnyOf(
                    qPurchaseOrder.code.contains(searchKeyword));
        }
        if (startTime != null) {
            builder.and(qPurchaseOrder.createdAt.goe(startTime));
        }

        if (endTime != null) {
            builder.and(qPurchaseOrder.createdAt.loe(endTime));
        }
        Long count = query().from(qPurchaseOrder)
                .where(builder)
                .select(qPurchaseOrder.id.count())
                .fetchOne();
        return count;
    }

    @Override
    public List<PurchaseOrderRes> getPurchaseOrders(String searchKeyword, Date startTime, Date endTime, int page) {
        BooleanBuilder builder = new BooleanBuilder();
        builder.and(qPurchaseOrder.deleted.eq(false));
        if (searchKeyword != null) {
            builder.andAnyOf(
                    qPurchaseOrder.code.contains(searchKeyword));
        }
        if (startTime != null) {
            builder.and(qPurchaseOrder.createdAt.goe(startTime));
        }

        if (endTime != null) {
            builder.and(qPurchaseOrder.createdAt.loe(endTime));
        }

        List<PurchaseOrderRes> purchaseOrders = query().from(qPurchaseOrder)
                .leftJoin(qUser).on(qUser.id.eq(qPurchaseOrder.userId))
                .where(builder)
                .orderBy(qPurchaseOrder.id.desc())
                .offset(page * PAGE_SIZE)
                .limit(PAGE_SIZE)
                .select(Projections.fields(PurchaseOrderRes.class,
                        qPurchaseOrder.id,
                        qPurchaseOrder.code,
                        qPurchaseOrder.userId,
                        qUser.name.as("userName"),
                        qPurchaseOrder.note,
                        qPurchaseOrder.importDate,
                        qPurchaseOrder.createdAt
                ))
                .fetch();

        if(purchaseOrders.isEmpty()) {
            return Collections.emptyList();
        }

        List<Integer> purchaseOrderIds = purchaseOrders.stream()
                .map(PurchaseOrderRes::getId)
                .toList();

        List<PurchaseOrderItemRes> purchaseOrderItems = query().from(qPurchaseOrderItem)
                .leftJoin(qModel).on(qPurchaseOrderItem.purchaseOrderId.eq(qModel.id))
                .leftJoin(qProduct).on(qProduct.id.eq(qModel.productId))
                .leftJoin(qUploadFile).on(qUploadFile.id.eq(qProduct.coverImage))
                .where(qPurchaseOrderItem.purchaseOrderId.in(purchaseOrderIds)
                        .and(qPurchaseOrderItem.deleted.eq(false)))
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

        Map<Integer, List<PurchaseOrderItemRes>> purchaseOrderItemMap = purchaseOrderItems.stream()
                .collect(Collectors.groupingBy(PurchaseOrderItemRes::getPurchaseOrderId));

        purchaseOrders.forEach(purchaseOrder -> purchaseOrder.setItems(purchaseOrderItemMap.getOrDefault(purchaseOrder.getId(), new ArrayList<>())));


        return purchaseOrders;
    }

    @Override
    public PurchaseOrderRes getPurchaseOrder(int purchaseOrderId) {
        BooleanBuilder builder = new BooleanBuilder();
        builder.and(qPurchaseOrder.deleted.eq(false));
        builder.and(qPurchaseOrder.id.eq(purchaseOrderId));

        PurchaseOrderRes purchaseOrder = query().from(qPurchaseOrder)
                .leftJoin(qUser).on(qUser.id.eq(qPurchaseOrder.userId))
                .where(builder)
                .select(Projections.fields(PurchaseOrderRes.class,
                        qPurchaseOrder.id,
                        qPurchaseOrder.code,
                        qPurchaseOrder.userId,
                        qUser.name.as("userName"),
                        qPurchaseOrder.note,
                        qPurchaseOrder.importDate,
                        qPurchaseOrder.createdAt
                ))
                .fetchOne();

        if(purchaseOrder == null) {
            return null;
        }

        List<PurchaseOrderItemRes> purchaseOrderItems = query().from(qPurchaseOrderItem)
                .leftJoin(qModel).on(qPurchaseOrderItem.modelId.eq(qModel.id))
                .leftJoin(qProduct).on(qProduct.id.eq(qModel.productId))
                .leftJoin(qUploadFile).on(qUploadFile.id.eq(qProduct.coverImage))
                .where(qPurchaseOrderItem.purchaseOrderId.eq(purchaseOrderId)
                        .and(qPurchaseOrderItem.deleted.eq(false)))
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
        purchaseOrder.setItems(purchaseOrderItems);

        return purchaseOrder;
    }
}
