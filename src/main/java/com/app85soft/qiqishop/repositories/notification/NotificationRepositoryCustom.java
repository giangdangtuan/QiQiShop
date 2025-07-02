package com.app85soft.qiqishop.repositories.notification;

import com.app85soft.qiqishop.dto.response.notification.OrderNotificationRes;

import java.util.List;

public interface NotificationRepositoryCustom {
    List<OrderNotificationRes> getNotifications();

    void markNotificationAsRead();

    Integer unreadCount();
}
