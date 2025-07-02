package com.app85soft.qiqishop.services.notification;

import com.app85soft.qiqishop.dto.response.chat_message.ChatMessageRes;
import com.app85soft.qiqishop.dto.response.notification.OrderNotificationRes;

import java.util.List;

public interface NotificationService {
    List<OrderNotificationRes> getNotifications();

    void markNotificationAsRead();

    Integer unreadCount();
}
