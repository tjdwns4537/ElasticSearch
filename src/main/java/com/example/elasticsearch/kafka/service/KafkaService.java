package com.example.elasticsearch.kafka.service;

import com.example.elasticsearch.article.domain.ArticleEls;
import com.example.elasticsearch.crawler.service.CrawlerService;
import com.example.elasticsearch.elastic.service.ElasticService;
import com.example.elasticsearch.elastic.service.ThemaElasticService;
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
public class KafkaService {

    @Autowired private KafkaTemplate<String, Object> kafkaTemplate;
    @Autowired private final CrawlerService crawlerService;

    public void sendNaverArticleMessage(String message) {

        ListenableFuture<SendResult<String, Object>> future =
                kafkaTemplate.send(Indices.NAVER_ARTICLE_CRAWLER_TOPIC, message);

        future.addCallback(new ListenableFutureCallback<SendResult<String, Object>>() {

            @Override
            public void onSuccess(SendResult<String, Object> result) {
                crawlerService.readArticle(); // 뉴스 기사 크롤링 수행 -> ELS doc으로 인덱싱
                log.info("Sent message=[ {} ] with offset=[ {} ]",message, result.getRecordMetadata().offset());
            }
            @Override
            public void onFailure(Throwable ex) {
                log.info("Unable to send message=[ {} ] due to : {}", message, ex.getMessage());
            }
        });
    }

    public void sendNaverThemaMessage(String message) {

        ListenableFuture<SendResult<String, Object>> future =
                kafkaTemplate.send(Indices.NAVER_THEMA_CRAWLER_TOPIC, message);

        future.addCallback(new ListenableFutureCallback<SendResult<String, Object>>() {

            @Override
            public void onSuccess(SendResult<String, Object> result) {
                crawlerService.naverReadThema(); // 테마 크롤링 후 저장
                log.info("Sent message=[ {} ] with offset=[ {} ]",message, result.getRecordMetadata().offset());
            }
            @Override
            public void onFailure(Throwable ex) {
                log.info("Unable to send message=[ {} ] due to : {}", message, ex.getMessage());
            }
        });
    }

    @KafkaListener(topics = Indices.NAVER_ARTICLE_CRAWLER_TOPIC, groupId = Indices.NAVER_ARTICLE_CRAWLER_TOPIC_GROUPID1)
    public void listenGroupArticleGroup(ArticleEls message) {
//        elasticService.articleSave(message);
        log.info("Received Message in group article: {}",message.getTitle());
    }

    @KafkaListener(topics = Indices.NAVER_THEMA_CRAWLER_TOPIC_GROUPID1, groupId = Indices.NAVER_ARTICLE_CRAWLER_TOPIC_GROUPID1)
    public void listenGroupThemaGroup(Thema message) {
//        themaElasticService.themaSave(message);
        log.info("Received Message in group thema: {}", message.getThemaName());
    }
}
