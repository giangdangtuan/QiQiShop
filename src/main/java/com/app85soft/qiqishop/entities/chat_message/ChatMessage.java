package com.app85soft.qiqishop.entities.chat_message;

import com.app85soft.qiqishop.entities.BaseEntity;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Table(name = "chat_message")
public class ChatMessage extends BaseEntity {
    int senderId;
    int recipientId;
    String content;
    boolean seen;
}
