package com.app85soft.qiqishop.controller;

import com.app85soft.qiqishop.entities.chat_message.ChatMessage;
import com.app85soft.qiqishop.services.chat_message.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class ChatMessageController {
    private final ChatService chatService;
    private final SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/chat.send")
    public void sendMessage(ChatMessage message) {
        ChatMessage saved = chatService.save(message);
        messagingTemplate.convertAndSendToUser(
                String.valueOf(saved.getRecipientId()), "/queue/messages", saved);
    }
}
