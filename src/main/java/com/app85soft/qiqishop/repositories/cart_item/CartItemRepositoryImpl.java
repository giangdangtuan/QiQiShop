package com.app85soft.qiqishop.repositories.cart_item;

import com.app85soft.qiqishop.dto.constant.ActiveStatus;
import com.app85soft.qiqishop.dto.response.cart.CartRes;
import com.app85soft.qiqishop.dto.response.file.UploadFileRes;
import com.app85soft.qiqishop.entities.cart.QCart;
import com.app85soft.qiqishop.entities.cart.QCartItem;
import com.app85soft.qiqishop.entities.model.QModel;
import com.app85soft.qiqishop.entities.product.QProduct;
import com.app85soft.qiqishop.entities.promotion.QPromotion;
import com.app85soft.qiqishop.entities.promotion.QPromotionModel;
import com.app85soft.qiqishop.entities.upload_file.QUploadFile;
import com.app85soft.qiqishop.repositories.BaseRepository;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Repository
public class CartItemRepositoryImpl extends BaseRepository implements CartItemRepositoryCustom {
    QCart qCart = QCart.cart;
    QCartItem qCartItem = QCartItem.cartItem;
    QModel qModel = QModel.model;
    QProduct qProduct = QProduct.product;
    QPromotion qPromotion = QPromotion.promotion;
    QPromotionModel qPromotionModel = QPromotionModel.promotionModel;
    QUploadFile qUploadFile = QUploadFile.uploadFile;

    @Override
    public List<CartRes.CartItemRes> getListCartIteam(List<Integer> ids, int userId) {
        long now = System.currentTimeMillis();

        BooleanExpression promotionCondition = qPromotionModel.id.isNotNull()
                .and(qPromotion.startTime.loe(now))
                .and(qPromotion.endTime.goe(now));

        return query().from(qCartItem)
                .innerJoin(qCart).on(qCartItem.cartId.eq(qCart.id))
                .innerJoin(qModel).on(qModel.id.eq(qCartItem.modelId))
                .innerJoin(qProduct).on(qProduct.id.eq(qModel.productId))
                .innerJoin(qUploadFile).on(qUploadFile.id.eq(qProduct.coverImage))
                .leftJoin(qPromotionModel).on(qPromotionModel.modelId.eq(qModel.id)
                        .and(qPromotionModel.status.eq(ActiveStatus.ACTIVE)))
                .leftJoin(qPromotion).on(qPromotion.id.eq(qPromotionModel.promotionId)
                        .and(qPromotion.status.eq(ActiveStatus.ACTIVE))
                        .and(qPromotion.startTime.loe(now))
                        .and(qPromotion.endTime.goe(now))
                        .and(qPromotion.deleted.eq(false)))
                .where(qCartItem.id.in(ids)
                        .and(qCart.userId.eq(userId)))
                .select(Projections.fields(CartRes.CartItemRes.class,
                        qCartItem.id,
                        qProduct.id.as("productId"),
                        qProduct.name.as("productName"),
                        qUploadFile.originUrl.as("originUrl"),
                        qUploadFile.thumbUrl.as("thumbUrl"),
                        qModel.id.as("modelId"),
                        qModel.name.as("modelName"),
                        qCartItem.quantity,
                        qModel.stock.as("stock"),
                        qProduct.weight.as("weight"),
                        qModel.price.as("originalPrice"),
                        Expressions.cases()
                                .when(promotionCondition)
                                .then(qModel.price.subtract(
                                        qModel.price.multiply(qPromotionModel.discountPercentage.doubleValue()).divide(100)
                                ))
                                .otherwise(qModel.price)
                                .as("finalPrice"),
                        Expressions.cases()
                                .when(promotionCondition)
                                .then(
                                        qModel.price.subtract(
                                                qModel.price.multiply(qPromotionModel.discountPercentage.doubleValue()).divide(100)
                                        ).multiply(qCartItem.quantity)
                                )
                                .otherwise(qModel.price.multiply(qCartItem.quantity))
                                .as("totalAmount")
                ))
                .fetch();
    }

    @Override
    @Transactional
    public void deleteAllByIdsAndUserId(List<Integer> ids, int userId) {
        BooleanBuilder builder = new BooleanBuilder();
        builder.and(qCartItem.id.in(ids));
        builder.and(qCartItem.cartId.in(
                query().select(qCart.id)
                        .from(qCart)
                        .where(qCart.userId.eq(userId))
        ));
        query().delete(qCartItem)
                .where(builder)
                .execute();
    }
}
