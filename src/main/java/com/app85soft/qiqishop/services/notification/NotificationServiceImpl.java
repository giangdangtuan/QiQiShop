package com.app85soft.qiqishop.services.notification;

import com.app85soft.qiqishop.dto.response.notification.OrderNotificationRes;
import com.app85soft.qiqishop.repositories.notification.NotificationRepository;
import com.app85soft.qiqishop.services.BaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl extends BaseService implements NotificationService {
    private final NotificationRepository notificationRepository;

    @Override
    public List<OrderNotificationRes> getNotifications() {
        return notificationRepository.getNotifications();
    }

    @Override
    public void markNotificationAsRead() {
        notificationRepository.markNotificationAsRead();
    }

    @Override
    public Integer unreadCount() {
        return notificationRepository.unreadCount();
    }
}
