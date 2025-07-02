package com.app85soft.qiqishop.repositories.transaction;

import com.app85soft.qiqishop.dto.constant.PaymentGateway;
import com.app85soft.qiqishop.dto.constant.TransactionStatus;
import com.app85soft.qiqishop.dto.response.category.CategoryListRes;
import com.app85soft.qiqishop.dto.response.transaction.TransactionRes;
import com.app85soft.qiqishop.entities.order.QOrder;
import com.app85soft.qiqishop.entities.transaction.QTransactions;
import com.app85soft.qiqishop.entities.user.QUser;
import com.app85soft.qiqishop.repositories.BaseRepository;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;

@Slf4j
@Repository
public class TransactionRepositoryImpl extends BaseRepository implements TransactionRepositoryCustom {
    private final QTransactions qTransactions = QTransactions.transactions;
    private final QUser qUser = QUser.user;
    private final QOrder qOrder = QOrder.order;

    @Override
    public long countTransaction(TransactionStatus status, PaymentGateway gateway, String searchKeyword) {
        BooleanBuilder builder = new BooleanBuilder();
        if (status != null) {
            builder.and(qTransactions.status.eq(status));
        }
        builder.and(qTransactions.deleted.eq(false));
        if (searchKeyword != null) {
            builder.andAnyOf(
                    qTransactions.code.contains(searchKeyword));
        }
        if (gateway != null) {
            builder.and(qTransactions.paymentGateway.eq(gateway));
        }

        Long count = query().from(qTransactions)
                .where(builder)
                .select(qTransactions.id.count())
                .fetchOne();
        return count == null ? 0 : count;
    }

    @Override
    public List<TransactionRes> getTransactions(TransactionStatus status, PaymentGateway gateway, String searchKeyword, int page) {
        BooleanBuilder builder = new BooleanBuilder();
        if (status != null) {
            builder.and(qTransactions.status.eq(status));
        }
        builder.and(qTransactions.deleted.eq(false));
        if (searchKeyword != null) {
            builder.andAnyOf(
                    qTransactions.code.contains(searchKeyword));
        }
        if (gateway != null) {
            builder.and(qTransactions.paymentGateway.eq(gateway));
        }

        return query().from(qTransactions)
                .leftJoin(qUser).on(qUser.id.eq(qTransactions.userId))
                .leftJoin(qOrder).on(qOrder.id.eq(qTransactions.orderId))
                .where(builder)
                .select(Projections.fields(TransactionRes.class,
                        qTransactions.id,
                        qTransactions.code,
                        qTransactions.referenceCode,
                        qTransactions.userId,
                        qUser.name.as("userName"),
                        qTransactions.orderId,
                        qOrder.code.as("orderCode"),
                        qTransactions.amount,
                        qTransactions.paymentGateway,
                        qTransactions.description,
                        qTransactions.paymentMethod,
                        qTransactions.status,
                        qTransactions.createdAt
                ))
                .fetch();
    }

    @Override
    public TransactionRes getTransactionDetail(int transactionId) {
        BooleanBuilder builder = new BooleanBuilder();
        builder.and(qTransactions.id.eq(transactionId));
        builder.and(qTransactions.deleted.eq(false));

        TransactionRes transaction = query().from(qTransactions)
                .leftJoin(qUser).on(qUser.id.eq(qTransactions.userId))
                .leftJoin(qOrder).on(qOrder.id.eq(qTransactions.orderId))
                .where(builder)
                .select(Projections.fields(TransactionRes.class,
                        qTransactions.id,
                        qTransactions.code,
                        qTransactions.referenceCode,
                        qTransactions.userId,
                        qUser.name.as("userName"),
                        qTransactions.orderId,
                        qOrder.code.as("orderCode"),
                        qTransactions.amount,
                        qTransactions.paymentGateway,
                        qTransactions.description,
                        qTransactions.paymentMethod,
                        qTransactions.status,
                        qTransactions.createdAt
                ))
                .fetchOne();

        return transaction;
    }
}
