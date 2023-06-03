package com.example.elasticsearch.kafka.service;

import com.example.elasticsearch.article.domain.ArticleEls;
import com.example.elasticsearch.crawler.service.CrawlerService;
import com.example.elasticsearch.helper.Indices;
import com.example.elasticsearch.thema.domain.Thema;
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
public class MainKafkaService {

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;
    @Autowired
    private final CrawlerService crawlerService;

    public void sendMethodMessage(String message) {
        ListenableFuture<SendResult<String, Object>> future =
                kafkaTemplate.send(Indices.METHOD_TOPIC, message);

        future.addCallback(new ListenableFutureCallback<SendResult<String, Object>>() {

            @Override
            public void onSuccess(SendResult<String, Object> result) {
                log.info(message);
            }
            @Override
            public void onFailure(Throwable ex) {
                log.error("Unable to send message=[ {} ] due to : {}", "기사 크롤링", ex.getMessage());
            }
        });
    }

    @KafkaListener(topics = Indices.METHOD_TOPIC, groupId = Indices.METHOD_TOPIC_GROUPID)
    public void listenGroupThemaGroup(String message) {
        crawlerService.crawlingArticle();

    }
}
