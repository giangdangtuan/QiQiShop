package com.app85soft.qiqishop.services.chat_message;

import com.app85soft.qiqishop.dto.response.chat_message.ChatMessageRes;
import com.app85soft.qiqishop.dto.response.chat_message.ChatUserSummaryRes;
import com.app85soft.qiqishop.entities.chat_message.ChatMessage;

import java.util.List;

public interface ChatService {
    public ChatMessage save(ChatMessage message);

    List<ChatMessageRes> getChatHistory(Integer user1, Integer user2);

    List<ChatUserSummaryRes> getUsersMessagedAdmin();

    void markMessagesAsRead(int senderId, int recipientId);
}
