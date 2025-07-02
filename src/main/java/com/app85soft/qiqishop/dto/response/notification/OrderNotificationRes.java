package com.app85soft.qiqishop.dto.response.notification;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderNotificationRes {
    int id;
    Integer orderId;
    String title;
    String message;
    boolean seen;
    Date createdAt;
}
