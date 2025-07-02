package com.app85soft.qiqishop.controller;

import com.app85soft.qiqishop.annotations.NoRequireAuth;
import com.app85soft.qiqishop.dto.response.BaseResponse;
import com.app85soft.qiqishop.dto.response.category.CategoryListRes;
import com.app85soft.qiqishop.dto.response.notification.OrderNotificationRes;
import com.app85soft.qiqishop.services.notification.NotificationService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/")
@RequiredArgsConstructor
public class NotificationController {
    private final NotificationService notificationService;

    @Operation(summary = "Get list notification.")
    @GetMapping("v1/notification/list")
    public ResponseEntity<BaseResponse<List<OrderNotificationRes>>> getNotifications() {
        return ResponseEntity.ok(new BaseResponse<>(notificationService.getNotifications()));
    }

    @PutMapping("v1/notification/mark-as-read")
    public ResponseEntity<Void> markMessagesAsRead() {
        notificationService.markNotificationAsRead();
        return ResponseEntity.ok().build();
    }

    @GetMapping("/v1/notification/unread-count")
    public ResponseEntity<BaseResponse<?>> getUnreadCount() {
        return ResponseEntity.ok(new BaseResponse<>(notificationService.unreadCount()));
    }
}
