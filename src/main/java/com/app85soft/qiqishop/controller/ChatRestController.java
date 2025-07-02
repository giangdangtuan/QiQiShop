package com.app85soft.qiqishop.controller;

import com.app85soft.qiqishop.dto.response.BaseResponse;
import com.app85soft.qiqishop.dto.response.chat_message.ChatMessageRes;
import com.app85soft.qiqishop.dto.response.chat_message.ChatUserSummaryRes;
import com.app85soft.qiqishop.services.chat_message.ChatService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ChatRestController {

    private final ChatService chatService;

    @Operation(summary = "Get chat history")
    @GetMapping("chat/history")
    public List<ChatMessageRes> getHistory(@RequestParam Integer user1, @RequestParam Integer user2) {
        return chatService.getChatHistory(user1, user2);
    }

    @GetMapping("/v1/chat/users-messaged-admin")
    public ResponseEntity<BaseResponse<List<ChatUserSummaryRes>>> getUsersMessagedAdmin() {
        return ResponseEntity.ok(new BaseResponse<>(chatService.getUsersMessagedAdmin()));
    }

    @PutMapping("/mark-as-read")
    public ResponseEntity<Void> markMessagesAsRead(
            @RequestParam int senderId,
            @RequestParam int recipientId) {
        chatService.markMessagesAsRead(senderId, recipientId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/v1/chat/unread-count")
    public ResponseEntity<BaseResponse<?>> getUnreadCount() {
        return ResponseEntity.ok(new BaseResponse<>(chatService.unreadCount()));
    }
}

