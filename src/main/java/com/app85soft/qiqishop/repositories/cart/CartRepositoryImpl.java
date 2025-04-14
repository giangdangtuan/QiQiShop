package com.app85soft.qiqishop.repositories.cart;

import com.app85soft.qiqishop.dto.constant.ActiveStatus;
import com.app85soft.qiqishop.dto.response.cart.CartRes;
import com.app85soft.qiqishop.entities.cart.QCart;
import com.app85soft.qiqishop.entities.cart.QCartItem;
import com.app85soft.qiqishop.entities.model.QModel;
import com.app85soft.qiqishop.entities.product.QProduct;
import com.app85soft.qiqishop.entities.promotion.QPromotion;
import com.app85soft.qiqishop.entities.promotion.QPromotionModel;
import com.app85soft.qiqishop.entities.user.QUser;
import com.app85soft.qiqishop.repositories.BaseRepository;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;

@Slf4j
@Repository
public class CartRepositoryImpl  extends BaseRepository implements CartRepositoryCustom {
    private final QCart qCart = QCart.cart;
    private final QCartItem qCartItem = QCartItem.cartItem;
    private final QModel qModel = QModel.model;
    private final QUser qUser = QUser.user;
    private final QPromotion qPromotion = QPromotion.promotion;
    private final QPromotionModel qPromotionModel = QPromotionModel.promotionModel;
    private final QProduct qProduct = QProduct.product;


    @Override
    public CartRes getCartByUserId(int userId) {
        long now = System.currentTimeMillis();

        BooleanExpression promotionCondition = qPromotionModel.id.isNotNull()
                .and(qPromotion.startTime.loe(now))
                .and(qPromotion.endTime.goe(now));

        List<CartRes.CartItemRes> items = query().from(qCart)
                .innerJoin(qCartItem).on(qCartItem.cartId.eq(qCart.id))
                .innerJoin(qModel).on(qModel.id.eq(qCartItem.modelId))
                .innerJoin(qProduct).on(qProduct.id.eq(qModel.productId))
                .leftJoin(qPromotionModel).on(qPromotionModel.modelId.eq(qModel.id)
                        .and(qPromotionModel.status.eq(ActiveStatus.ACTIVE)))
                .leftJoin(qPromotion).on(qPromotion.id.eq(qPromotionModel.promotionId)
                        .and(qPromotion.status.eq(ActiveStatus.ACTIVE))
                        .and(qPromotion.startTime.loe(now))
                        .and(qPromotion.endTime.goe(now))
                        .and(qPromotion.deleted.eq(false)))
                .where(qCart.userId.eq(userId))
                .select(Projections.fields(CartRes.CartItemRes.class,
                        qCartItem.id,
                        qProduct.id.as("productId"),
                        qProduct.name.as("productName"),
                        qModel.id.as("modelId"),
                        qModel.name.as("modelName"),
                        qModel.coverImage.as("cover_image"),
                        qCartItem.quantity,
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

        return CartRes.builder()
                .userId(userId)
                .items(items)
                .build();
    }

    @Override
    public List<Integer> getAllCartIteamIdToCheckExist(List<Integer> cartIteamIds) {
        BooleanBuilder builder = new BooleanBuilder();
        builder.and(qCartItem.id.in(cartIteamIds));
        builder.and(qCartItem.deleted.eq(false));

        return query().from(qCartItem)
                .where(builder)
                .select(qCartItem.id)
                .fetch();
    }

}
