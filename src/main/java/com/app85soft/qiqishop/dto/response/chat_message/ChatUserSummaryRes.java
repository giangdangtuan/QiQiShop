package com.app85soft.qiqishop.dto.response.chat_message;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ChatUserSummaryRes {
    int userId;
    String userName;
    Integer avatarId;
    String originUrl;
    String thumbUrl;
    String lastMessage;
    Date lastMessageTime;
    Long unreadCount;
}
