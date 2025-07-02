package com.app85soft.qiqishop.repositories.notification;

import com.app85soft.qiqishop.entities.notification.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Integer>, NotificationRepositoryCustom {
}
