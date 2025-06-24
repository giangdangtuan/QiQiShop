package com.app85soft.qiqishop.repositories.order;

import com.app85soft.qiqishop.dto.constant.OrderStatus;
import com.app85soft.qiqishop.dto.constant.PaymentMethod;
import com.app85soft.qiqishop.dto.request.rating.AddRatingReq;
import com.app85soft.qiqishop.dto.response.address.AddressRes;
import com.app85soft.qiqishop.dto.response.order.OrderDertailRes;
import com.app85soft.qiqishop.dto.response.order.OrderListRes;
import com.app85soft.qiqishop.dto.response.order.OrderRes;
import com.app85soft.qiqishop.entities.address.QAddress;
import com.app85soft.qiqishop.entities.address.QDistrict;
import com.app85soft.qiqishop.entities.address.QProvince;
import com.app85soft.qiqishop.entities.address.QWard;
import com.app85soft.qiqishop.entities.model.QModel;
import com.app85soft.qiqishop.entities.order.QOrder;
import com.app85soft.qiqishop.entities.order.QOrderDetail;
import com.app85soft.qiqishop.entities.product.QProduct;
import com.app85soft.qiqishop.entities.rating.QRating;
import com.app85soft.qiqishop.entities.upload_file.QUploadFile;
import com.app85soft.qiqishop.entities.user.QUser;
import com.app85soft.qiqishop.repositories.BaseRepository;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.app85soft.qiqishop.util.Constants.PAGE_SIZE;

@Slf4j
@Repository
public class OrderRepositoryImpl extends BaseRepository implements OrderRepositoryCustom {
    private final QOrder qOrder = QOrder.order;
    private final QAddress qAddress = QAddress.address;
    private final QOrderDetail qOrderDetail = QOrderDetail.orderDetail;
    private final QModel qModel = QModel.model;
    private final QProduct qProduct = QProduct.product;
    private final QProvince qProvince = QProvince.province;
    private final QDistrict qDistrict = QDistrict.district;
    private final QWard qWard = QWard.ward;
    private final QUser qUser = QUser.user;
    private final QRating qRating = QRating.rating;
    private final QUploadFile qUploadFile = QUploadFile.uploadFile;

    @Override
    public long countOrder(OrderStatus status, String orderCode, PaymentMethod paymentMethod) {
        BooleanBuilder builder = new BooleanBuilder();
        if (status != null) {
            builder.and(qOrder.status.eq(status));
        }
        builder.and(qOrder.deleted.eq(false));
        if (orderCode != null) {
            builder.andAnyOf(
                    qOrder.code.contains(orderCode));
        }
        if (paymentMethod != null) {
            builder.and(qOrder.paymentMethod.eq(paymentMethod));
        }
        Long count = query().from(qOrder)
                .where(builder)
                .select(qOrder.id.count())
                .fetchOne();
        return count == null ? 0 : count;
    }

    @Override
    public List<OrderListRes> getOrders(OrderStatus status, String orderCode, PaymentMethod paymentMethod, Integer userId, int page) {
        BooleanBuilder builder = new BooleanBuilder();
        if (status != null) {
            builder.and(qOrder.status.eq(status));
        }
        builder.and(qOrder.deleted.eq(false));
        if (orderCode != null) {
            builder.and(qOrder.code.contains(orderCode));
        }
        if (paymentMethod != null) {
            builder.and(qOrder.paymentMethod.eq(paymentMethod));
        }
        if (userId != null) {
            builder.and(qOrder.userId.eq(userId));
        }

        List<OrderListRes> orders = query()
                .select(Projections.constructor(OrderListRes.class,
                        qOrder.id,
                        qOrder.code,
                        qOrder.userId,
                        qUser.name.as("userName"),
                        qOrder.totalPrice,
                        qOrder.paymentMethod,
                        qOrder.shippingCost,
                        qOrder.note,
                        qOrder.status,
                        qOrder.rated
                ))
                .from(qOrder)
                .leftJoin(qUser).on(qOrder.userId.eq(qUser.id))
                .where(builder)
                .orderBy(qOrder.id.desc())
                .offset(page * PAGE_SIZE)
                .limit(PAGE_SIZE)
                .fetch();

        for (OrderListRes order : orders) {
            List<OrderDertailRes> orderDetails = query()
                    .select(Projections.constructor(OrderDertailRes.class,
                            qOrderDetail.id,
                            qProduct.id,
                            qModel.id,
                            qModel.name,
                            qProduct.name,
                            qProduct.coverImage.as("productImage"),
                            qOrderDetail.amount,
                            qOrderDetail.originalPrice,
                            qOrderDetail.finalPrice,
                            qUploadFile.originUrl.as("originUrl"),
                            qUploadFile.thumbUrl.as("thumbUrl")
                    ))
                    .from(qOrderDetail)
                    .leftJoin(qModel).on(qOrderDetail.modelId.eq(qModel.id))
                    .leftJoin(qProduct).on(qModel.productId.eq(qProduct.id))
                    .leftJoin(qUploadFile).on(qProduct.coverImage.eq(qUploadFile.id))
                    .where(qOrderDetail.orderId.eq(order.getId())
                            .and(qOrderDetail.deleted.eq(false)))
                    .fetch();

            order.setOrderDetails(orderDetails);
        }
        return orders;
    }

    @Override
    public OrderRes getOrder(int orderId) {
        OrderRes order = query()
                .select(Projections.constructor(OrderRes.class,
                        qOrder.id,
                        qOrder.code,
                        qOrder.userId,
                        qUser.name.as("userName"),
                        qOrder.totalPrice,
                        qOrder.paymentMethod,
                        qOrder.shippingCost,
                        qOrder.note,
                        qOrder.status,
                        qOrder.rated,
                        Projections.constructor(AddressRes.class,
                                qAddress.id,
                                qAddress.userId,
                                qAddress.consignee,
                                qAddress.phone,
                                qProvince.name.as("provinceName"),
                                qDistrict.name.as("districtName"),
                                qWard.name.as("wardName"),
                                qAddress.detailAddress,
                                qAddress.isDefault)
                ))
                .from(qOrder)
                .leftJoin(qUser).on(qOrder.userId.eq(qUser.id))
                .leftJoin(qAddress).on(qOrder.addressId.eq(qAddress.id))
                .leftJoin(qProvince).on(qAddress.provinceId.eq(qProvince.id))
                .leftJoin(qDistrict).on(qAddress.districtId.eq(qDistrict.id))
                .leftJoin(qWard).on(qAddress.wardId.eq(qWard.id))
                .where(qOrder.id.eq(orderId))
                .fetchOne();

        List<OrderDertailRes> orderDetails = query()
                .select(Projections.constructor(OrderDertailRes.class,
                        qOrderDetail.id,
                        qModel.id.as("modelId"),
                        qModel.name.as("modelName"),
                        qProduct.name.as("productName"),
                        qProduct.coverImage.as("productImage"),
                        qOrderDetail.amount,
                        qOrderDetail.originalPrice,
                        qOrderDetail.finalPrice,
                        qUploadFile.originUrl.as("originUrl"),
                        qUploadFile.thumbUrl.as("thumbUrl")
                ))
                .from(qOrderDetail)
                .leftJoin(qModel).on(qOrderDetail.modelId.eq(qModel.id))
                .leftJoin(qProduct).on(qModel.productId.eq(qProduct.id))
                .leftJoin(qUploadFile).on(qProduct.coverImage.eq(qUploadFile.id))
                .where(qOrderDetail.orderId.eq(order.getId())
                        .and(qOrderDetail.deleted.eq(false)))
                .fetch();
        order.setOrderDetails(orderDetails);

        return order;
    }

    @Override
    public boolean existsByOrderIdAndProductId(int orderId, List<AddRatingReq.RatingItem> ratingItems) {
        List<Integer> productIds = ratingItems.stream()
                .map(AddRatingReq.RatingItem::getProductId)
                .distinct()
                .toList();

        BooleanBuilder builder = new BooleanBuilder();
        builder.and(qOrder.id.eq(orderId));
        builder.and(qOrderDetail.orderId.eq(qOrder.id));
        builder.and(qModel.id.eq(qOrderDetail.modelId));
        builder.and(qProduct.id.eq(qModel.productId));
        builder.and(qProduct.id.in(productIds));

        // Kiểm tra có ít nhất 1 sản phẩm hợp lệ trong đơn hàng
        Long count = query().from(qOrder)
                .join(qOrderDetail).on(qOrderDetail.orderId.eq(qOrder.id))
                .join(qModel).on(qModel.id.eq(qOrderDetail.modelId))
                .join(qProduct).on(qProduct.id.eq(qModel.productId))
                .where(builder)
                .select(qProduct.id.countDistinct()) // có thể dùng count() hoặc fetchFirst()
                .fetchOne();

        return count != null && count == productIds.size(); // đảm bảo tất cả sản phẩm đều hợp lệ
    }
}
