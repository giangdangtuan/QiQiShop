package com.app85soft.qiqishop.repositories.chat_message;

import com.app85soft.qiqishop.dto.response.chat_message.ChatUserSummaryRes;

import java.util.List;

public interface ChatMessageRepositoryCustom {
    List<ChatUserSummaryRes> getUsersMessagedAdmin();

    void markMessagesAsRead(int senderId, int recipientId);
}
