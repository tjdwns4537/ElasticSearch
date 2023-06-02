package com.example.elasticsearch.kafka.service;

import com.example.elasticsearch.helper.Indices;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

@Service
@Slf4j
@RequiredArgsConstructor
public class KafkaService {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    public void sendMessage(String message) {

        ListenableFuture<SendResult<String, String>> future =
                kafkaTemplate.send(Indices.NAVER_CRAWLER_TOPIC, message);

        future.addCallback(new ListenableFutureCallback<SendResult<String, String>>() {

            @Override
            public void onSuccess(SendResult<String, String> result) {
                log.info("Sent message=[ {} ] with offset=[ {} ]",message, result.getRecordMetadata().offset());
            }
            @Override
            public void onFailure(Throwable ex) {
                log.info("Unable to send message=[ {} ] due to : {}", message, ex.getMessage());
            }
        });
    }

    @KafkaListener(topics = Indices.NAVER_CRAWLER_TOPIC, groupId = Indices.NAVER_CRAWLER_TOPIC_GROUPID1)
    public void listenGroupFoo(String message) {
        System.out.println("Received Message in group foo: " + message);
    }
}
