package com.example.elasticsearch.kafka;

import com.example.elasticsearch.helper.Indices;
import com.example.elasticsearch.kafka.service.CrawlingKafkaService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.messaging.handler.annotation.Header;

//@EmbeddedKafka(partitions = 3,
//        brokerProperties = {
//                "listeners=PLAINTEXT://localhost:9092"
//        },
//        ports = { 9092 })
//@SpringBootTest
public class ServiceTest {

//    @Autowired
//    private KafkaTemplate<String, Object> kafkaTemplate;
//
//    @Test
//    @DisplayName("전달 테스트")
//    public void sendTest() {
//        kafkaTemplate.send(Indices.TA_TOPIC, "HI1");
//        kafkaTemplate.send(Indices.TA_TOPIC, "HI2");
//
//    }
//
//    @KafkaListener(topics = Indices.TA_TOPIC, groupId = Indices.TA_GROUPID)
//    public void listenGroupTAGroup1(@Header(KafkaHeaders.RECEIVED_MESSAGE_KEY) String key,
//                                    @Header(KafkaHeaders.RECEIVED_PARTITION_ID) int partition,
//                                    String stock) {
//        System.out.println("kafka test1 : "+stock);
//        System.out.println("partition test1 : "+ partition);
//        System.out.println("key test1 : "+ key);
//    }
//
//    @KafkaListener(topics = Indices.TA_TOPIC, groupId = Indices.TA_GROUPID)
//    public void listenGroupTAGroup2(@Header(KafkaHeaders.RECEIVED_MESSAGE_KEY) String key,
//                                    @Header(KafkaHeaders.RECEIVED_PARTITION_ID) int partition,
//                                    String stock) {
//        System.out.println("kafka test2 : "+stock);
//        System.out.println("partition test2 : "+ partition);
//        System.out.println("key test2 : "+ key);
//    }
}
