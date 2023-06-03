package com.example.elasticsearch.kafka.service;

import com.example.elasticsearch.article.domain.ArticleEls;
import com.example.elasticsearch.crawler.service.CrawlerService;
import com.example.elasticsearch.elastic.service.ElasticService;
import com.example.elasticsearch.elastic.service.ThemaElasticService;
import com.example.elasticsearch.helper.Indices;
import com.example.elasticsearch.helper.Timer;
import com.example.elasticsearch.stock.domain.StockElasticDto;
import com.example.elasticsearch.thema.domain.Thema;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class CrawlingKafkaService {

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    @Autowired
    private final CrawlerService crawlerService;

    public void sendNaverArticleMessage(String message) {

        ListenableFuture<SendResult<String, Object>> future =
                kafkaTemplate.send(Indices.NAVER_ARTICLE_CRAWLER_TOPIC, message);

        future.addCallback(new ListenableFutureCallback<SendResult<String, Object>>() {

            @Override
            public void onSuccess(SendResult<String, Object> result) {
            }
            @Override
            public void onFailure(Throwable ex) {
                log.error("Unable to send message=[ {} ] due to : {}", "기사 크롤링", ex.getMessage());
            }
        });
    }

    public void sendNaverThemaMessage1(int uri) {
        kafkaTemplate.send(Indices.NAVER_THEMA_CRAWLER_TOPIC, uri);
    }

    public void sendNaverThemaMessage2(int uri) {
        kafkaTemplate.send(Indices.NAVER_THEMA_CRAWLER_TOPIC, uri);
    }

    public void sendNaverThemaMessage3(int uri) {
        kafkaTemplate.send(Indices.NAVER_THEMA_CRAWLER_TOPIC, uri);
    }

    public void sendNaverThemaMessage4(int uri) {
        kafkaTemplate.send(Indices.NAVER_THEMA_CRAWLER_TOPIC, uri);
    }

    public void sendNaverThemaMessage5(int uri) {
        kafkaTemplate.send(Indices.NAVER_THEMA_CRAWLER_TOPIC, uri);
    }

    public void sendNaverThemaMessage6(int uri) {
        kafkaTemplate.send(Indices.NAVER_THEMA_CRAWLER_TOPIC, uri);
    }

    public void sendNaverThemaMessage7(int uri) {
        kafkaTemplate.send(Indices.NAVER_THEMA_CRAWLER_TOPIC, uri);
    }

    @KafkaListener(topics = Indices.NAVER_ARTICLE_CRAWLER_TOPIC, groupId = Indices.NAVER_ARTICLE_CRAWLER_TOPIC_GROUPID)
    public void listenGroupArticleGroup(String message) {
        crawlerService.crawlingArticle();
    }

    @KafkaListener(topics = Indices.NAVER_THEMA_CRAWLER_TOPIC, groupId = Indices.NAVER_THEMA_CRAWLER_TOPIC_GROUPID1)
    public void listenGroupThemaGroup1(int uri) {
        crawlerService.naverReadThema(uri);
    }

    @KafkaListener(topics = Indices.NAVER_THEMA_CRAWLER_TOPIC, groupId = Indices.NAVER_THEMA_CRAWLER_TOPIC_GROUPID2)
    public void listenGroupThemaGroup2(int uri) {
        crawlerService.naverReadThema(uri);
    }

    @KafkaListener(topics = Indices.NAVER_THEMA_CRAWLER_TOPIC, groupId = Indices.NAVER_THEMA_CRAWLER_TOPIC_GROUPID3)
    public void listenGroupThemaGroup3(int uri) {
        crawlerService.naverReadThema(uri);
    }

    @KafkaListener(topics = Indices.NAVER_THEMA_CRAWLER_TOPIC, groupId = Indices.NAVER_THEMA_CRAWLER_TOPIC_GROUPID4)
    public void listenGroupThemaGroup4(int uri) {
        crawlerService.naverReadThema(uri);
    }

    @KafkaListener(topics = Indices.NAVER_THEMA_CRAWLER_TOPIC, groupId = Indices.NAVER_THEMA_CRAWLER_TOPIC_GROUPID5)
    public void listenGroupThemaGroup5(int uri) {
        crawlerService.naverReadThema(uri);
    }

    @KafkaListener(topics = Indices.NAVER_THEMA_CRAWLER_TOPIC, groupId = Indices.NAVER_THEMA_CRAWLER_TOPIC_GROUPID6)
    public void listenGroupThemaGroup6(int uri) {
        crawlerService.naverReadThema(uri);
    }

    @KafkaListener(topics = Indices.NAVER_THEMA_CRAWLER_TOPIC, groupId = Indices.NAVER_THEMA_CRAWLER_TOPIC_GROUPID7)
    public void listenGroupThemaGroup7(int uri) {
        crawlerService.naverReadThema(uri);
    }
}
