package com.app85soft.qiqishop.repositories.chat_message;

import com.app85soft.qiqishop.entities.chat_message.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, Integer>, ChatMessageRepositoryCustom {
    List<ChatMessage> findBySenderIdAndRecipientIdOrRecipientIdAndSenderId(
            Integer sender1, Integer recipient1, Integer sender2, Integer recipient2);
}
