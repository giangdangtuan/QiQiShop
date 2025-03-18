package com.app85soft.qiqishop.other_service.kafka;

import com.app85soft.qiqishop.util.Util;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;

import static com.app85soft.qiqishop.configuration.KafkaTopicConfig.REPLY_TOPIC;

@Log4j2
@Service
@RequiredArgsConstructor
public class KafkaRequestReplyService {

    private final ReplyingKafkaTemplate<Object, Object, Object> replyingKafkaTemplate;

    public Object sendMessage(String topic, Object key, Object message) {
        Message<Object> sendMessage = MessageBuilder.withPayload(message)
                .setHeader(KafkaHeaders.KEY, key)
                .setHeader(KafkaHeaders.TOPIC, topic)
                .setHeader(KafkaHeaders.CORRELATION_ID, Util.genUUID().getBytes(StandardCharsets.UTF_8))
                .setHeader(KafkaHeaders.REPLY_TOPIC, REPLY_TOPIC) // Đặt topic nhận phản hồi
                .build();
        try {
            Message<?> res = replyingKafkaTemplate.sendAndReceive(sendMessage).join();
            if (res == null) {
                return null;
            }
            return res.getPayload();
        } catch (Exception e) {
            log.error("sendMessage", e);
            return null;
        }
    }
}