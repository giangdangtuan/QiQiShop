package com.app85soft.qiqishop.dto.response.chat_message;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ChatMessageRes {
    int id;
    int senderId;
    String senderName;
    int recipientId;
    String recipientName;
    String content;
    Date createdAt;
}
