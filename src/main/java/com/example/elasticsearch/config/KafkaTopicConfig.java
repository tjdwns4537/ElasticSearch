package com.example.elasticsearch.config;

import com.example.elasticsearch.helper.Indices;
import org.apache.kafka.clients.admin.*;
import org.apache.kafka.common.KafkaFuture;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaAdmin;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaTopicConfig {

    @Value(value = "${spring.kafka.bootstrap-servers}")
    private String bootstrapAddress;

    @Bean
    public KafkaAdmin kafkaAdmin() {
        Map<String, Object> configs = new HashMap<>();
        configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
        return new KafkaAdmin(configs);
    }

    @Bean
    public NewTopic articleThemaTopic() {
        return new NewTopic(Indices.NAVER_ARTICLE_CRAWLER_TOPIC, 1, (short) 1);
    }

    @Bean
    public NewTopic naverThemaTopic() {
        return new NewTopic(Indices.NAVER_THEMA_CRAWLER_TOPIC, 8, (short) 1);
    }

    @Bean
    public NewTopic naverMethodTopic() {
        return new NewTopic(Indices.METHOD_TOPIC, 1, (short) 1);
    }
}
