package com.example.elasticsearch.kafka.service;

import com.example.elasticsearch.crawler.service.CrawlerService;
import com.example.elasticsearch.helper.Indices;
import com.example.elasticsearch.redis.repository.RecommendStockRedisRepo;
import com.example.elasticsearch.search.service.SearchService;
import com.example.elasticsearch.stock.service.StockService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.kafka.support.SendResult;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

@Service
@Slf4j
@RequiredArgsConstructor
public class CrawlingKafkaService {

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    @Autowired
    private final CrawlerService crawlerService;

    @Autowired
    private final SearchService searchService;

    public void sendNaverThemaMessage1(int uri) {kafkaTemplate.send(Indices.NAVER_THEMA_CRAWLER_TOPIC,"p1",uri);}

    public void sendNaverThemaMessage2(int uri) {kafkaTemplate.send(Indices.NAVER_THEMA_CRAWLER_TOPIC,"p2", uri);}

    public void sendNaverThemaMessage3(int uri) {
        kafkaTemplate.send(Indices.NAVER_THEMA_CRAWLER_TOPIC,"p3",uri);
    }

    public void sendNaverThemaMessage4(int uri) {
        kafkaTemplate.send(Indices.NAVER_THEMA_CRAWLER_TOPIC,"p4",uri);
    }

    public void sendNaverThemaMessage5(int uri) {
        kafkaTemplate.send(Indices.NAVER_THEMA_CRAWLER_TOPIC,"p5",uri);
    }

    public void sendNaverThemaMessage6(int uri) {
        kafkaTemplate.send(Indices.NAVER_THEMA_CRAWLER_TOPIC,"p6",uri);
    }

    public void sendNaverThemaMessage7(int uri) {
        kafkaTemplate.send(Indices.NAVER_THEMA_CRAWLER_TOPIC,"p7",uri);
    }

    public void sendTAMessage1(String stock) {
        kafkaTemplate.send(Indices.TA_TOPIC,"ta1", stock);
    }

    public void sendTAMessage2(String stock2) {
        kafkaTemplate.send(Indices.TA_TOPIC,"ta2", stock2);
    }

    @KafkaListener(topics = Indices.TA_TOPIC, groupId = Indices.TA_GROUPID)
    public void listenGroupTAGroup1(String stock) {
        searchService.saveBestStock(stock);
    }

    @KafkaListener(topics = Indices.TA_TOPIC, groupId = Indices.TA_GROUPID2)
    public void listenGroupTAGroup2(String stock2) {
        searchService.saveBestStock(stock2);
    }

    @KafkaListener(topics = Indices.NAVER_ARTICLE_CRAWLER_TOPIC, groupId = Indices.NAVER_ARTICLE_CRAWLER_TOPIC_GROUPID)
    public void listenGroupArticleGroup(String message) {
        crawlerService.crawlingArticle();
    }

    @KafkaListener(topics = Indices.NAVER_THEMA_CRAWLER_TOPIC, groupId = Indices.NAVER_THEMA_CRAWLER_TOPIC_GROUPID1)
    public void listenGroupThemaGroup1(@Header(KafkaHeaders.RECEIVED_MESSAGE_KEY) String key,
                                       @Header(KafkaHeaders.RECEIVED_PARTITION_ID) int partition,
                                       int uri) {
        crawlerService.naverReadThema(false,1);
    }

    @KafkaListener(topics = Indices.NAVER_THEMA_CRAWLER_TOPIC, groupId = Indices.NAVER_THEMA_CRAWLER_TOPIC_GROUPID2)
    public void listenGroupThemaGroup2(@Header(KafkaHeaders.RECEIVED_MESSAGE_KEY) String key,
                                       @Header(KafkaHeaders.RECEIVED_PARTITION_ID) int partition,
                                       int uri) {
        crawlerService.naverReadThema(false,2);
    }

    @KafkaListener(topics = Indices.NAVER_THEMA_CRAWLER_TOPIC, groupId = Indices.NAVER_THEMA_CRAWLER_TOPIC_GROUPID3)
    public void listenGroupThemaGroup3(@Header(KafkaHeaders.RECEIVED_MESSAGE_KEY) String key,
                                       @Header(KafkaHeaders.RECEIVED_PARTITION_ID) int partition,
                                       int uri) {
        crawlerService.naverReadThema(false,3);
    }

    @KafkaListener(topics = Indices.NAVER_THEMA_CRAWLER_TOPIC, groupId = Indices.NAVER_THEMA_CRAWLER_TOPIC_GROUPID4)
    public void listenGroupThemaGroup4(@Header(KafkaHeaders.RECEIVED_MESSAGE_KEY) String key,
                                       @Header(KafkaHeaders.RECEIVED_PARTITION_ID) int partition,
                                       int uri) {
        crawlerService.naverReadThema(false,4);
    }

    @KafkaListener(topics = Indices.NAVER_THEMA_CRAWLER_TOPIC, groupId = Indices.NAVER_THEMA_CRAWLER_TOPIC_GROUPID5)
    public void listenGroupThemaGroup5(@Header(KafkaHeaders.RECEIVED_MESSAGE_KEY) String key,
                                       @Header(KafkaHeaders.RECEIVED_PARTITION_ID) int partition,
                                       int uri) {
        crawlerService.naverReadThema(false,5);
    }

    @KafkaListener(topics = Indices.NAVER_THEMA_CRAWLER_TOPIC, groupId = Indices.NAVER_THEMA_CRAWLER_TOPIC_GROUPID6)
    public void listenGroupThemaGroup6(@Header(KafkaHeaders.RECEIVED_MESSAGE_KEY) String key,
                                       @Header(KafkaHeaders.RECEIVED_PARTITION_ID) int partition) {
        crawlerService.naverReadThema(false,6);
    }

    @KafkaListener(topics = Indices.NAVER_THEMA_CRAWLER_TOPIC, groupId = Indices.NAVER_THEMA_CRAWLER_TOPIC_GROUPID7)
    public void listenGroupThemaGroup7(@Header(KafkaHeaders.RECEIVED_MESSAGE_KEY) String key,
                                       @Header(KafkaHeaders.RECEIVED_PARTITION_ID) int partition) {
        crawlerService.naverReadThema(false,7);
    }
}
