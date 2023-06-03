package com.example.elasticsearch.kafka.controller;

import com.example.elasticsearch.article.domain.ArticleEls;
import com.example.elasticsearch.kafka.service.CrawlingKafkaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
@Slf4j
public class KafkaTestController {

    @Autowired
    private final CrawlingKafkaService crawlingKafkaService;

    @GetMapping("/kafka")
    public void test() {
        ArticleEls articleEls = ArticleEls.of("테스트 중 !!");
//        kafkaService.sendMessage(articleEls);
    }
}
