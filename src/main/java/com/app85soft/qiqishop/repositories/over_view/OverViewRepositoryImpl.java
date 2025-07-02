package com.app85soft.qiqishop.repositories.over_view;

import com.app85soft.qiqishop.dto.response.over_view.StatPoint;
import com.app85soft.qiqishop.entities.order.QOrder;
import com.app85soft.qiqishop.entities.order.QOrderDetail;
import com.app85soft.qiqishop.entities.order.QOrderDetailBatch;
import com.app85soft.qiqishop.entities.purchase_orders.QStockBatch;
import com.app85soft.qiqishop.repositories.BaseRepository;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.StringTemplate;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Repository
public class OverViewRepositoryImpl extends BaseRepository implements OverViewRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public OverViewRepositoryImpl(EntityManager entityManager) {
        this.queryFactory = new JPAQueryFactory(entityManager);
    }

    @Override
    public BigDecimal getTotalProfit(Date fromDate, Date toDate) {
        QOrder qOrder = QOrder.order;
        QOrderDetail qDetail = QOrderDetail.orderDetail;
        QOrderDetailBatch qODB = QOrderDetailBatch.orderDetailBatch;
        QStockBatch qStock = QStockBatch.stockBatch;

        Tuple result = queryFactory
                .select(
                        qDetail.finalPrice.multiply(qDetail.amount).sum(),
                        qStock.unitCost.multiply(qODB.quantityAllocated).sum()
                )
                .from(qDetail)
                .join(qOrder).on(qDetail.orderId.eq(qOrder.id))
                .join(qODB).on(qODB.orderDetailId.eq(qDetail.id))
                .join(qStock).on(qStock.id.eq(qODB.stockBatchId))
                .where(
                        qOrder.createdAt.between(fromDate, toDate),
                        qOrder.deleted.isFalse(),
                        qDetail.deleted.isFalse(),
                        qODB.deleted.isFalse(),
                        qStock.deleted.isFalse()
                )
                .fetchOne();

        if (result == null) return BigDecimal.ZERO;

        BigDecimal revenue = result.get(0, BigDecimal.class);
        BigDecimal cost = result.get(1, BigDecimal.class);

        if (revenue == null) revenue = BigDecimal.ZERO;
        if (cost == null) cost = BigDecimal.ZERO;

        return revenue.subtract(cost);
    }

    @Override
    public List<StatPoint> queryStatPoints(Date from, Date to, ChronoUnit unit, boolean isRevenue) {
        QOrder qOrder = QOrder.order;
        QOrderDetail qDetail = QOrderDetail.orderDetail;

        // Biểu thức nhóm thời gian
        StringTemplate groupExpr = switch (unit) {
            case HOURS -> Expressions.stringTemplate("UNIX_TIMESTAMP(DATE_FORMAT({0}, '%Y-%m-%d %H:00:00'))", qOrder.createdAt);
            case DAYS -> Expressions.stringTemplate("UNIX_TIMESTAMP(DATE({0}))", qOrder.createdAt);
            case MONTHS -> Expressions.stringTemplate("UNIX_TIMESTAMP(DATE_FORMAT({0}, '%Y-%m-01'))", qOrder.createdAt);
            default -> throw new IllegalArgumentException("Unsupported unit: " + unit);
        };

        List<Tuple> tuples;

        if (isRevenue) {
            tuples = queryFactory
                    .select(groupExpr, qDetail.finalPrice.multiply(qDetail.amount).sum())
                    .from(qDetail)
                    .join(qOrder).on(qDetail.orderId.eq(qOrder.id))
                    .where(
                            qOrder.createdAt.between(from, to),
                            qOrder.deleted.isFalse(),
                            qDetail.deleted.isFalse()
                    )
                    .groupBy(groupExpr)
                    .orderBy(groupExpr.asc())
                    .fetch();
        } else {
            tuples = queryFactory
                    .select(groupExpr, qOrder.id.count())
                    .from(qOrder)
                    .where(
                            qOrder.createdAt.between(from, to),
                            qOrder.deleted.isFalse()
                    )
                    .groupBy(groupExpr)
                    .orderBy(groupExpr.asc())
                    .fetch();
        }

        return tuples.stream().map(tuple -> {
            // Dùng theo index: cột 0 là timestamp, cột 1 là value
            Number timestampNum = tuple.get(0, Number.class);
            Long timestamp = timestampNum != null ? timestampNum.longValue() : 0L;

            Number valueNum = tuple.get(1, Number.class);
            BigDecimal value = valueNum != null ? new BigDecimal(valueNum.toString()) : BigDecimal.ZERO;

            return new StatPoint(timestamp, value);
        }).collect(Collectors.toList());
    }
}
