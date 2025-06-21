package com.app85soft.qiqishop.repositories.chat_message;

import com.app85soft.qiqishop.dto.response.category.CategoryListRes;
import com.app85soft.qiqishop.dto.response.chat_message.ChatUserSummaryRes;
import com.app85soft.qiqishop.entities.chat_message.QChatMessage;
import com.app85soft.qiqishop.entities.upload_file.QUploadFile;
import com.app85soft.qiqishop.entities.user.QUser;
import com.app85soft.qiqishop.repositories.BaseRepository;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAUpdateClause;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Slf4j
@Repository
public class ChatMessageRepositoryImpl extends BaseRepository implements ChatMessageRepositoryCustom {
    private final QChatMessage qChatMessage = QChatMessage.chatMessage;
    private final QUser qUser = QUser.user;
    private final QUploadFile qUploadFile = QUploadFile.uploadFile;

    @PersistenceContext
    private EntityManager em;

    @Override
    public List<ChatUserSummaryRes> getUsersMessagedAdmin() {
        int adminId = 1;

        BooleanBuilder builder = new BooleanBuilder();
        builder.and(qChatMessage.deleted.eq(false));
        builder.and(qChatMessage.recipientId.eq(adminId));

        QChatMessage subMsg = new QChatMessage("subMsg");

        return query().from(qChatMessage)
                .join(qUser).on(qChatMessage.senderId.eq(qUser.id))
                .leftJoin(qUploadFile).on(qUser.avatarId.eq(qUploadFile.id).and(qUploadFile.deleted.isFalse()))
                .where(builder
                        .and(qChatMessage.createdAt.eq(
                                query().select(subMsg.createdAt.max())
                                        .from(subMsg)
                                        .where(subMsg.senderId.eq(qChatMessage.senderId)
                                                .and(subMsg.recipientId.eq(adminId)))
                        ))
                )
                .select(Projections.fields(ChatUserSummaryRes.class,
                        qUser.id.as("userId"),
                        qUser.name.as("userName"),
                        qUploadFile.originUrl.as("avatarUrl"),
                        qChatMessage.content.as("lastMessage"),
                        qChatMessage.createdAt.as("lastMessageTime"),
                        Expressions.as(
                                JPAExpressions.select(subMsg.id.count())
                                        .from(subMsg)
                                        .where(
                                                subMsg.senderId.eq(qUser.id)
                                                        .and(subMsg.recipientId.eq(adminId))
                                                        .and(subMsg.seen.eq(false))
                                                        .and(subMsg.deleted.isFalse())
                                        ), "unreadCount"
                        )
                ))
                .orderBy(qChatMessage.createdAt.desc())
                .fetch();
    }

    @Transactional
    @Override
    public void markMessagesAsRead(int senderId, int recipientId) {
        QChatMessage q = QChatMessage.chatMessage;

        new JPAUpdateClause(em, q)
                .where(q.senderId.eq(senderId)
                        .and(q.recipientId.eq(recipientId))
                        .and(q.seen.isFalse())
                        .and(q.deleted.isFalse()))
                .set(q.seen, true)
                .execute();
    }
}
