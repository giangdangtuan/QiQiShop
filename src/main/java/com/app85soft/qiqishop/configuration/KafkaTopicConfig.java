package com.app85soft.qiqishop.configuration;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.KafkaMessageListenerContainer;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;

import java.time.Duration;

@EnableKafka
@Configuration
public class KafkaTopicConfig {
    public static final String REPLY_TOPIC = "REPLY_TOPIC";

    @Bean
    public ReplyingKafkaTemplate<Object, Object, Object> replyingKafkaTemplate(ProducerFactory<Object, Object> pf, KafkaMessageListenerContainer<Object, Object> container) {
        final long KAFKA_TIME_OUT = 30;
        ReplyingKafkaTemplate<Object, Object, Object> kafkaTemplate = new ReplyingKafkaTemplate<>(pf, container);
        kafkaTemplate.setDefaultReplyTimeout(Duration.ofSeconds(KAFKA_TIME_OUT));
        return kafkaTemplate;
    }

    @Bean
    public KafkaMessageListenerContainer<Object, Object> replyContainer(ConsumerFactory<Object, Object> cf) {
        ContainerProperties containerProperties = new ContainerProperties(REPLY_TOPIC); // Topic nhận phản hồi
        return new KafkaMessageListenerContainer<>(cf, containerProperties);
    }

    @Bean
    public NewTopic kafkaReplyTopics() {
        return new NewTopic(REPLY_TOPIC, 10, (short) 1);
    }

}
