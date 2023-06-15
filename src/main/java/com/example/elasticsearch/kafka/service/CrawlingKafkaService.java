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
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class CrawlingKafkaService {

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplateString;

    @Autowired
    private final CrawlerService crawlerService;

    public void sendNaverThemaMessage(int uri, int p) {kafkaTemplate.send(Indices.NAVER_THEMA_CRAWLER_TOPIC, p,"T",uri);}

    public void sendTAMessage(String stockNumberArg, int p) {
        kafkaTemplateString.send(Indices.TA_TOPIC, p,"TA",  stockNumberArg);
    }

    @KafkaListener(topicPartitions = {
            @TopicPartition(topic = Indices.TA_TOPIC, partitions = {"0"})
    },
            groupId = Indices.TA_GROUPID)
    public void listenGroupTAGroup1(@Header(KafkaHeaders.RECEIVED_MESSAGE_KEY) String key,
                                    @Header(KafkaHeaders.RECEIVED_PARTITION_ID) int partition,
                                    String stockNumberArg, int p) {
        crawlerService.financialCrawler(stockNumberArg);
    }

    @KafkaListener(topicPartitions = {
            @TopicPartition(topic = Indices.TA_TOPIC, partitions = {"1"})
    },
            groupId = Indices.TA_GROUPID2)
    public void listenGroupTAGroup2(@Header(KafkaHeaders.RECEIVED_MESSAGE_KEY) String key,
                                    @Header(KafkaHeaders.RECEIVED_PARTITION_ID) int partition,
                                    String stockNumberArg, int p) {
        crawlerService.financialCrawler(stockNumberArg);
    }

    @KafkaListener(topicPartitions = {
            @TopicPartition(topic = Indices.TA_TOPIC, partitions = {"2"})
    },
            groupId = Indices.TA_GROUPID3)
    public void listenGroupTAGroup3(@Header(KafkaHeaders.RECEIVED_MESSAGE_KEY) String key,
                                    @Header(KafkaHeaders.RECEIVED_PARTITION_ID) int partition,
                                    String stockNumberArg, int p) {
        crawlerService.financialCrawler(stockNumberArg);
    }

    @KafkaListener(topicPartitions = {
            @TopicPartition(topic = Indices.TA_TOPIC, partitions = {"3"})
    },
            groupId = Indices.TA_GROUPID4)
    public void listenGroupTAGroup4(@Header(KafkaHeaders.RECEIVED_MESSAGE_KEY) String key,
                                    @Header(KafkaHeaders.RECEIVED_PARTITION_ID) int partition,
                                    String stockNumberArg, int p) {
        crawlerService.financialCrawler(stockNumberArg);
    }

    @KafkaListener(topicPartitions = {
            @TopicPartition(topic = Indices.TA_TOPIC, partitions = {"4"})
    },
            groupId = Indices.TA_GROUPID5)
    public void listenGroupTAGroup5(@Header(KafkaHeaders.RECEIVED_MESSAGE_KEY) String key,
                                    @Header(KafkaHeaders.RECEIVED_PARTITION_ID) int partition,
                                    String stockNumberArg, int p) {
        crawlerService.financialCrawler(stockNumberArg);
    }

    @KafkaListener(topicPartitions = {
            @TopicPartition(topic = Indices.TA_TOPIC, partitions = {"5"})
    },
            groupId = Indices.TA_GROUPID6)
    public void listenGroupTAGroup6(@Header(KafkaHeaders.RECEIVED_MESSAGE_KEY) String key,
                                    @Header(KafkaHeaders.RECEIVED_PARTITION_ID) int partition,
                                    String stockNumberArg, int p) {
        crawlerService.financialCrawler(stockNumberArg);
    }

    @KafkaListener(topicPartitions = {
            @TopicPartition(topic = Indices.TA_TOPIC, partitions = {"6"})
    },
            groupId = Indices.TA_GROUPID7)
    public void listenGroupTAGroup7(@Header(KafkaHeaders.RECEIVED_MESSAGE_KEY) String key,
                                    @Header(KafkaHeaders.RECEIVED_PARTITION_ID) int partition,
                                    String stockNumberArg, int p) {
        crawlerService.financialCrawler(stockNumberArg);
    }

    @KafkaListener(topicPartitions = {
            @TopicPartition(topic = Indices.TA_TOPIC, partitions = {"7"})
    },
            groupId = Indices.TA_GROUPID8)
    public void listenGroupTAGroup8(@Header(KafkaHeaders.RECEIVED_MESSAGE_KEY) String key,
                                    @Header(KafkaHeaders.RECEIVED_PARTITION_ID) int partition,
                                    String stockNumberArg, int p) {
        crawlerService.financialCrawler(stockNumberArg);
    }

    @KafkaListener(topicPartitions = {
            @TopicPartition(topic = Indices.TA_TOPIC, partitions = {"8"})
    },
            groupId = Indices.TA_GROUPID9)
    public void listenGroupTAGroup9(@Header(KafkaHeaders.RECEIVED_MESSAGE_KEY) String key,
                                    @Header(KafkaHeaders.RECEIVED_PARTITION_ID) int partition,
                                    String stockNumberArg, int p) {
        crawlerService.financialCrawler(stockNumberArg);
    }

    @KafkaListener(topicPartitions = {
            @TopicPartition(topic = Indices.TA_TOPIC, partitions = {"9"})
    },
            groupId = Indices.TA_GROUPID10)
    public void listenGroupTAGroup10(@Header(KafkaHeaders.RECEIVED_MESSAGE_KEY) String key,
                                    @Header(KafkaHeaders.RECEIVED_PARTITION_ID) int partition,
                                    String stockNumberArg, int p) {
        crawlerService.financialCrawler(stockNumberArg);
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
        crawlerService.naverReadThema(false,uri);
    }

    @KafkaListener(topicPartitions = {
            @TopicPartition(topic = Indices.NAVER_THEMA_CRAWLER_TOPIC, partitions = {"1"})
    },
            groupId = Indices.NAVER_THEMA_CRAWLER_TOPIC_GROUPID2)
    public void listenGroupThemaGroup2(@Header(KafkaHeaders.RECEIVED_MESSAGE_KEY) String key,
                                       @Header(KafkaHeaders.RECEIVED_PARTITION_ID) int partition,
                                       int uri, int p) {
        crawlerService.naverReadThema(false,uri);
    }

    @KafkaListener(topicPartitions = {
            @TopicPartition(topic = Indices.NAVER_THEMA_CRAWLER_TOPIC, partitions = {"3"})
    },
            groupId = Indices.NAVER_THEMA_CRAWLER_TOPIC_GROUPID3)
    public void listenGroupThemaGroup4(@Header(KafkaHeaders.RECEIVED_MESSAGE_KEY) String key,
                                       @Header(KafkaHeaders.RECEIVED_PARTITION_ID) int partition,
                                       int uri, int p) {
        crawlerService.naverReadThema(false,uri);
    }

    @KafkaListener(topicPartitions = {
            @TopicPartition(topic = Indices.NAVER_THEMA_CRAWLER_TOPIC, partitions = {"4"})
    },
            groupId = Indices.NAVER_THEMA_CRAWLER_TOPIC_GROUPID4)
    public void listenGroupThemaGroup5(@Header(KafkaHeaders.RECEIVED_MESSAGE_KEY) String key,
                                       @Header(KafkaHeaders.RECEIVED_PARTITION_ID) int partition,
                                       int uri, int p) {
        crawlerService.naverReadThema(false,uri);
    }

    @KafkaListener(topicPartitions = {
            @TopicPartition(topic = Indices.NAVER_THEMA_CRAWLER_TOPIC, partitions = {"5"})
    },
            groupId = Indices.NAVER_THEMA_CRAWLER_TOPIC_GROUPID5)
    public void listenGroupThemaGroup6(@Header(KafkaHeaders.RECEIVED_MESSAGE_KEY) String key,
                                       @Header(KafkaHeaders.RECEIVED_PARTITION_ID) int partition,
                                       int uri, int p) {
        crawlerService.naverReadThema(false,uri);
    }

    @KafkaListener(topicPartitions = {
            @TopicPartition(topic = Indices.NAVER_THEMA_CRAWLER_TOPIC, partitions = {"6"})
    },
            groupId = Indices.NAVER_THEMA_CRAWLER_TOPIC_GROUPID6)
    public void listenGroupThemaGroup7(@Header(KafkaHeaders.RECEIVED_MESSAGE_KEY) String key,
                                       @Header(KafkaHeaders.RECEIVED_PARTITION_ID) int partition,
                                       int uri, int p) {
        crawlerService.naverReadThema(false,uri);
    }

    @KafkaListener(topicPartitions = {
            @TopicPartition(topic = Indices.NAVER_THEMA_CRAWLER_TOPIC, partitions = {"2"})
    },
            groupId = Indices.NAVER_THEMA_CRAWLER_TOPIC_GROUPID7)
    public void listenGroupThemaGroup3(@Header(KafkaHeaders.RECEIVED_MESSAGE_KEY) String key,
                                       @Header(KafkaHeaders.RECEIVED_PARTITION_ID) int partition,
                                       int uri, int p) {
        crawlerService.naverReadThema(false,uri);
    }
}
