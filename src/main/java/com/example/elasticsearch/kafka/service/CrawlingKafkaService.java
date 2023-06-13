package com.example.elasticsearch.kafka.service;

import com.example.elasticsearch.crawler.service.CrawlerService;
import com.example.elasticsearch.helper.Indices;
import com.example.elasticsearch.search.service.SearchService;
import com.example.elasticsearch.stock.domain.FinanceStockRedis;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.TopicPartition;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class CrawlingKafkaService {

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    @Autowired
    private final CrawlerService crawlerService;

//    @Autowired
//    private final SearchService searchService;

    public void sendNaverThemaMessage(int uri, int p) {kafkaTemplate.send(Indices.NAVER_THEMA_CRAWLER_TOPIC, p,"T",uri);}

    public void sendTAMessage(int stockNumberArg, int p) {
        kafkaTemplate.send(Indices.TA_TOPIC, p,"TA", stockNumberArg);
    }

    @KafkaListener(topicPartitions = {
            @TopicPartition(topic = Indices.TA_TOPIC, partitions = {"0"})
    },
            groupId = Indices.TA_GROUPID)
    public void listenGroupTAGroup1(@Header(KafkaHeaders.RECEIVED_MESSAGE_KEY) String key,
                                    @Header(KafkaHeaders.RECEIVED_PARTITION_ID) int partition,
                                    int stockNumberArg, int p) {
        crawlerService.financialCrawler(stockNumberArg);

        log.info("kafka test1 : {}",stockNumberArg);
        log.info("partition test1 : {}",partition);
    }

    @KafkaListener(topicPartitions = {
            @TopicPartition(topic = Indices.TA_TOPIC, partitions = {"1"})
    },
            groupId = Indices.TA_GROUPID2)
    public void listenGroupTAGroup2(@Header(KafkaHeaders.RECEIVED_MESSAGE_KEY) String key,
                                    @Header(KafkaHeaders.RECEIVED_PARTITION_ID) int partition,
                                    int stockNumberArg, int p) {
        crawlerService.financialCrawler(stockNumberArg);
        log.info("kafka test2 : {}",stockNumberArg);
        log.info("partition test2 : {}",partition);
    }

    @KafkaListener(topicPartitions = {
            @TopicPartition(topic = Indices.TA_TOPIC, partitions = {"2"})
    },
            groupId = Indices.TA_GROUPID3)
    public void listenGroupTAGroup3(@Header(KafkaHeaders.RECEIVED_MESSAGE_KEY) String key,
                                    @Header(KafkaHeaders.RECEIVED_PARTITION_ID) int partition,
                                    int stockNumberArg, int p) {
        crawlerService.financialCrawler(stockNumberArg);
        log.info("kafka test3 : {}",stockNumberArg);
        log.info("partition test3 : {}",partition);
    }

    @KafkaListener(topicPartitions = {
            @TopicPartition(topic = Indices.TA_TOPIC, partitions = {"3"})
    },
            groupId = Indices.TA_GROUPID3)
    public void listenGroupTAGroup4(@Header(KafkaHeaders.RECEIVED_MESSAGE_KEY) String key,
                                    @Header(KafkaHeaders.RECEIVED_PARTITION_ID) int partition,
                                    int stockNumberArg, int p) {
        crawlerService.financialCrawler(stockNumberArg);
        log.info("kafka test4 : {}",stockNumberArg);
        log.info("partition test4 : {}",partition);
    }

    @KafkaListener(topicPartitions = {
            @TopicPartition(topic = Indices.TA_TOPIC, partitions = {"4"})
    },
            groupId = Indices.TA_GROUPID3)
    public void listenGroupTAGroup5(@Header(KafkaHeaders.RECEIVED_MESSAGE_KEY) String key,
                                    @Header(KafkaHeaders.RECEIVED_PARTITION_ID) int partition,
                                    int stockNumberArg, int p) {
        crawlerService.financialCrawler(stockNumberArg);
        log.info("kafka test5 : {}",stockNumberArg);
        log.info("partition test5 : {}",partition);
    }

    @KafkaListener(topicPartitions = {
            @TopicPartition(topic = Indices.NAVER_ARTICLE_CRAWLER_TOPIC, partitions = {"0"})
    },
            groupId = Indices.NAVER_ARTICLE_CRAWLER_TOPIC_GROUPID)
    public void listenGroupArticleGroup(String message) {
        crawlerService.crawlingArticle();
    }

    @KafkaListener(topicPartitions = {
            @TopicPartition(topic = Indices.NAVER_THEMA_CRAWLER_TOPIC, partitions = {"0"})
    },
            groupId = Indices.NAVER_THEMA_CRAWLER_TOPIC_GROUPID1)
    public void listenGroupThemaGroup1(@Header(KafkaHeaders.RECEIVED_MESSAGE_KEY) String key,
                                       @Header(KafkaHeaders.RECEIVED_PARTITION_ID) int partition,
                                       int uri, int p) {
        log.info("thema-partition : {}", partition);
        crawlerService.naverReadThema(false,uri);
    }

    @KafkaListener(topicPartitions = {
            @TopicPartition(topic = Indices.NAVER_THEMA_CRAWLER_TOPIC, partitions = {"1"})
    },
            groupId = Indices.NAVER_THEMA_CRAWLER_TOPIC_GROUPID2)
    public void listenGroupThemaGroup2(@Header(KafkaHeaders.RECEIVED_MESSAGE_KEY) String key,
                                       @Header(KafkaHeaders.RECEIVED_PARTITION_ID) int partition,
                                       int uri, int p) {
        log.info("thema-partition : {}", partition);
        crawlerService.naverReadThema(false,uri);
    }

    @KafkaListener(topicPartitions = {
            @TopicPartition(topic = Indices.NAVER_THEMA_CRAWLER_TOPIC, partitions = {"3"})
    },
            groupId = Indices.NAVER_THEMA_CRAWLER_TOPIC_GROUPID3)
    public void listenGroupThemaGroup4(@Header(KafkaHeaders.RECEIVED_MESSAGE_KEY) String key,
                                       @Header(KafkaHeaders.RECEIVED_PARTITION_ID) int partition,
                                       int uri, int p) {
        log.info("thema-partition : {}", partition);
        crawlerService.naverReadThema(false,uri);
    }

    @KafkaListener(topicPartitions = {
            @TopicPartition(topic = Indices.NAVER_THEMA_CRAWLER_TOPIC, partitions = {"4"})
    },
            groupId = Indices.NAVER_THEMA_CRAWLER_TOPIC_GROUPID4)
    public void listenGroupThemaGroup5(@Header(KafkaHeaders.RECEIVED_MESSAGE_KEY) String key,
                                       @Header(KafkaHeaders.RECEIVED_PARTITION_ID) int partition,
                                       int uri, int p) {
        log.info("thema-partition : {}", partition);
        crawlerService.naverReadThema(false,uri);
    }

    @KafkaListener(topicPartitions = {
            @TopicPartition(topic = Indices.NAVER_THEMA_CRAWLER_TOPIC, partitions = {"5"})
    },
            groupId = Indices.NAVER_THEMA_CRAWLER_TOPIC_GROUPID5)
    public void listenGroupThemaGroup6(@Header(KafkaHeaders.RECEIVED_MESSAGE_KEY) String key,
                                       @Header(KafkaHeaders.RECEIVED_PARTITION_ID) int partition,
                                       int uri, int p) {
        log.info("thema-partition : {}", partition);
        crawlerService.naverReadThema(false,uri);
    }

    @KafkaListener(topicPartitions = {
            @TopicPartition(topic = Indices.NAVER_THEMA_CRAWLER_TOPIC, partitions = {"6"})
    },
            groupId = Indices.NAVER_THEMA_CRAWLER_TOPIC_GROUPID6)
    public void listenGroupThemaGroup7(@Header(KafkaHeaders.RECEIVED_MESSAGE_KEY) String key,
                                       @Header(KafkaHeaders.RECEIVED_PARTITION_ID) int partition,
                                       int uri, int p) {
        log.info("thema-partition : {}", partition);
        crawlerService.naverReadThema(false,uri);
    }
}
