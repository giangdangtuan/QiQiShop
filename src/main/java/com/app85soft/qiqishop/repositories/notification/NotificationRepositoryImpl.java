package com.app85soft.qiqishop.repositories.notification;

import com.app85soft.qiqishop.dto.response.notification.OrderNotificationRes;
import com.app85soft.qiqishop.entities.notification.QNotification;
import com.app85soft.qiqishop.repositories.BaseRepository;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Repository
public class NotificationRepositoryImpl extends BaseRepository implements NotificationRepositoryCustom {
    private final QNotification qNotification = QNotification.notification;

    @Override
    public List<OrderNotificationRes> getNotifications() {
        BooleanBuilder builder = new BooleanBuilder();
        builder.and(qNotification.deleted.eq(false));

        return query().from(qNotification)
                .where(builder)
                .orderBy(qNotification.id.desc())
                .select(Projections.fields(OrderNotificationRes.class,
                        qNotification.id,
                        qNotification.orderId,
                        qNotification.title,
                        qNotification.message,
                        qNotification.seen,
                        qNotification.createdAt
                ))
                .fetch();
    }

    @Transactional
    @Override
    public void markNotificationAsRead() {
        query().update(qNotification)
                .set(qNotification.seen, true)
                .where(qNotification.deleted.eq(false).and(qNotification.seen.eq(false)))
                .execute();
    }


    @Override
    public Integer unreadCount() {
        Long count = query().select(qNotification.count())
                .from(qNotification)
                .where(qNotification.deleted.eq(false).and(qNotification.seen.eq(false)))
                .fetchOne();
        return count != null ? count.intValue() : 0;
    }


}
