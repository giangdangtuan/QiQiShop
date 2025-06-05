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
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.JPAExpressions;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Slf4j
@Repository
public class CartRepositoryImpl extends BaseRepository implements CartRepositoryCustom {

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

        QPromotionModel subPromotionModel = new QPromotionModel("subPromotionModel");
        QPromotion subPromotion = new QPromotion("subPromotion");

        // Subquery: lấy discountPercentage nếu có promotion, kiểu JPQLQuery<Integer>
        var discountPercentageSubQuery = JPAExpressions
                .select(subPromotionModel.discountPercentage)
                .from(subPromotionModel)
                .join(subPromotion).on(subPromotionModel.promotionId.eq(subPromotion.id))
                .where(
                        subPromotionModel.modelId.eq(qModel.id),
                        subPromotionModel.status.eq(ActiveStatus.ACTIVE),
                        subPromotion.status.eq(ActiveStatus.ACTIVE),
                        subPromotion.deleted.isFalse(),
                        subPromotion.startTime.loe(now),
                        subPromotion.endTime.goe(now)
                )
                .limit(1);

        // Chuyển JPQLQuery thành NumberExpression để dùng trong biểu thức số học
        NumberExpression<Integer> discountPercentageExpr = Expressions.numberTemplate(
                Integer.class, "({0})", discountPercentageSubQuery);

        // Tính giá finalPrice sau khuyến mãi
        NumberExpression<BigDecimal> finalPriceExpr = Expressions.cases()
                .when(discountPercentageExpr.isNotNull())
                .then(
                        qModel.price.subtract(
                                qModel.price.multiply(discountPercentageExpr.doubleValue()).divide(100)
                        )
                )
                .otherwise(qModel.price);

        // Tính tổng tiền totalAmount
        NumberExpression<BigDecimal> totalAmountExpr = Expressions.cases()
                .when(discountPercentageExpr.isNotNull())
                .then(
                        qModel.price.subtract(
                                qModel.price.multiply(discountPercentageExpr.doubleValue()).divide(100)
                        ).multiply(qCartItem.quantity)
                )
                .otherwise(qModel.price.multiply(qCartItem.quantity));

        List<CartRes.CartItemRes> items = query().from(qCart)
                .innerJoin(qCartItem).on(qCartItem.cartId.eq(qCart.id))
                .innerJoin(qModel).on(qModel.id.eq(qCartItem.modelId))
                .innerJoin(qProduct).on(qProduct.id.eq(qModel.productId))
                .where(qCart.userId.eq(userId))
                .select(Projections.fields(CartRes.CartItemRes.class,
                        qCartItem.id,
                        qProduct.id.as("productId"),
                        qProduct.name.as("productName"),
                        qModel.id.as("modelId"),
                        qModel.name.as("modelName"),
                        qCartItem.quantity,
                        qModel.stock.as("stock"),
                        qProduct.weight.as("weight"),
                        qModel.price.as("originalPrice"),
                        finalPriceExpr.as("finalPrice"),
                        totalAmountExpr.as("totalAmount")
                ))
                .fetch();

        return CartRes.builder()
                .userId(userId)
                .items(items)
                .build();
    }


    @Override
    public List<Integer> getAllCartIteamIdToCheckExist(List<Integer> cartIteamIds, int userId) {
        BooleanBuilder builder = new BooleanBuilder();
        builder.and(qCartItem.id.in(cartIteamIds));
        builder.and(qCartItem.deleted.eq(false));
        builder.and(qCartItem.cartId.eq(qCart.id));
        builder.and(qCart.userId.eq(userId));

        return query().from(qCartItem)
                .join(qCart).on(qCartItem.cartId.eq(qCart.id))
                .where(builder)
                .select(qCartItem.id)
                .fetch();
    }

}
