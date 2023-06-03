package com.example.elasticsearch.config;

import com.example.elasticsearch.helper.Indices;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.HashMap;
import java.util.Map;

@EnableKafka
@Configuration
public class KafkaConsumerConfig {

    @Value(value = "${spring.kafka.bootstrap-servers}")
    private String bootstrapAddress;

    String articleGroupId = Indices.NAVER_ARTICLE_CRAWLER_TOPIC_GROUPID;
    String themaGroupId1 = Indices.NAVER_THEMA_CRAWLER_TOPIC_GROUPID1;
    String themaGroupId2 = Indices.NAVER_THEMA_CRAWLER_TOPIC_GROUPID2;
    String themaGroupId3 = Indices.NAVER_THEMA_CRAWLER_TOPIC_GROUPID3;
    String themaGroupId4 = Indices.NAVER_THEMA_CRAWLER_TOPIC_GROUPID4;
    String themaGroupId5 = Indices.NAVER_THEMA_CRAWLER_TOPIC_GROUPID5;
    String themaGroupId6 = Indices.NAVER_THEMA_CRAWLER_TOPIC_GROUPID6;
    String themaGroupId7 = Indices.NAVER_THEMA_CRAWLER_TOPIC_GROUPID7;

    @Bean
    public ConsumerFactory<String, Object> consumerArticleFactoryGroup1() {
        return factory(articleGroupId);
    }

    @Bean
    public ConsumerFactory<String, Object> consumerFactoryGroup1() {
        return factory(themaGroupId1);
    }

    @Bean
    public ConsumerFactory<String, Object> consumerFactoryGroup2() {
        return factory(themaGroupId2);
    }

    @Bean
    public ConsumerFactory<String, Object> consumerFactoryGroup3() {
        return factory(themaGroupId3);
    }

    @Bean
    public ConsumerFactory<String, Object> consumerFactoryGroup4() {
        return factory(themaGroupId4);
    }

    @Bean
    public ConsumerFactory<String, Object> consumerFactoryGroup5() {
        return factory(themaGroupId5);
    }

    @Bean
    public ConsumerFactory<String, Object> consumerFactoryGroup6() {
        return factory(themaGroupId6);
    }

    @Bean
    public ConsumerFactory<String, Object> consumerFactoryGroup7() {
        return factory(themaGroupId7);
    }

    @Bean
    public KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, Object>> kafkaListenerContainerFactoryGroupArticle() {
        ConcurrentKafkaListenerContainerFactory<String, Object> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerArticleFactoryGroup1());
        // Configure other properties as needed
        return factory;
    }

    @Bean
    public KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, Object>> kafkaListenerContainerFactoryGroup1() {
        ConcurrentKafkaListenerContainerFactory<String, Object> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactoryGroup1());
        // Configure other properties as needed
        return factory;
    }

    @Bean
    public KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, Object>> kafkaListenerContainerFactoryGroup2() {
        ConcurrentKafkaListenerContainerFactory<String, Object> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactoryGroup2());
        // Configure other properties as needed
        return factory;
    }

    @Bean
    public KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, Object>> kafkaListenerContainerFactoryGroup3() {
        ConcurrentKafkaListenerContainerFactory<String, Object> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactoryGroup3());
        // Configure other properties as needed
        return factory;
    }

    @Bean
    public KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, Object>> kafkaListenerContainerFactoryGroup4() {
        ConcurrentKafkaListenerContainerFactory<String, Object> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactoryGroup4());
        // Configure other properties as needed
        return factory;
    }

    @Bean
    public KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, Object>> kafkaListenerContainerFactoryGroup5() {
        ConcurrentKafkaListenerContainerFactory<String, Object> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactoryGroup5());
        // Configure other properties as needed
        return factory;
    }

    @Bean
    public KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, Object>> kafkaListenerContainerFactoryGroup6() {
        ConcurrentKafkaListenerContainerFactory<String, Object> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactoryGroup6());
        // Configure other properties as needed
        return factory;
    }

    @Bean
    public KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, Object>> kafkaListenerContainerFactoryGroup7() {
        ConcurrentKafkaListenerContainerFactory<String, Object> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactoryGroup7());
        // Configure other properties as needed
        return factory;
    }


    public ConsumerFactory<String, Object> factory(String separateGroupId) {
        Map<String, Object> props = new HashMap<>();
        props.put(
                ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,
                bootstrapAddress);
        props.put(
                ConsumerConfig.GROUP_ID_CONFIG,
                separateGroupId);
        props.put(
                ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,
                StringDeserializer.class);
        props.put(
                ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,
                JsonDeserializer.class);
        props.put(
                JsonDeserializer.TRUSTED_PACKAGES,
                "*");

        ObjectMapper objectMapper = new ObjectMapper();
        return new DefaultKafkaConsumerFactory<>(
                props,
                new StringDeserializer(),
                new JsonDeserializer<>(Object.class, objectMapper)
        );
    }


//    @Bean
//    public ConcurrentKafkaListenerContainerFactory<String, Object> kafkaListenerContainerFactory() {
//        ConcurrentKafkaListenerContainerFactory<String, Object> factory = new ConcurrentKafkaListenerContainerFactory<>();
//        factory.setConsumerFactory(consumerFactory());
//        return factory;
//    }
}
