package com.app85soft.qiqishop.services.chat_message;

import com.app85soft.qiqishop.dto.response.chat_message.ChatMessageRes;
import com.app85soft.qiqishop.dto.response.chat_message.ChatUserSummaryRes;
import com.app85soft.qiqishop.entities.chat_message.ChatMessage;
import com.app85soft.qiqishop.entities.user.User;
import com.app85soft.qiqishop.repositories.chat_message.ChatMessageRepository;
import com.app85soft.qiqishop.repositories.user.UserRepository;
import com.app85soft.qiqishop.services.BaseService;
import com.app85soft.qiqishop.services.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatServiceImpl extends BaseService implements ChatService {
    private final ChatMessageRepository chatMessageRepository;
    private final UserRepository userRepository;

    @Override
    public ChatMessage save(ChatMessage message) {
//        User user = getUser();
        return chatMessageRepository.save(message);
    }

    @Override
    public List<ChatMessageRes> getChatHistory(Integer user1, Integer user2) {
//        User user = getUser();
        List<ChatMessage> messages = chatMessageRepository
                .findBySenderIdAndRecipientIdOrRecipientIdAndSenderId(user1, user2, user1, user2);

        return messages.stream().map(msg -> {
            User sender = userRepository.findById(msg.getSenderId()).orElseThrow();
            User recipient = userRepository.findById(msg.getRecipientId()).orElseThrow();
            return new ChatMessageRes(
                    msg.getId(),
                    msg.getSenderId(),
                    sender.getName(),
                    msg.getRecipientId(),
                    recipient.getName(),
                    msg.getContent(),
                    msg.getCreatedAt()
            );
        }).toList();
    }

    @Override
    public List<ChatUserSummaryRes> getUsersMessagedAdmin() {
        return chatMessageRepository.getUsersMessagedAdmin();
    }

    @Override
    public void markMessagesAsRead(int senderId, int recipientId) {
        chatMessageRepository.markMessagesAsRead(senderId, recipientId);
    }

    @Override
    public Integer unreadCount() {
        User user = getUser();
        return chatMessageRepository.unreadCount(user.getId());
    }
}
