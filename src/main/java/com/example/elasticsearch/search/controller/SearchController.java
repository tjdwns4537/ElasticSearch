package com.example.elasticsearch.search.controller;

import com.example.elasticsearch.crawler.service.CrawlerService;
import com.example.elasticsearch.elastic.service.ElasticCustomService;
import com.example.elasticsearch.elastic.service.ElasticService;
import com.example.elasticsearch.elastic.service.ThemaElasticService;
import com.example.elasticsearch.helper.Indices;
import com.example.elasticsearch.redis.repository.ArticleRedisRepository;
import com.example.elasticsearch.search.repository.SearchRepository;
import com.example.elasticsearch.sentiment.service.KoreanSentiment;
import com.example.elasticsearch.thema.domain.Thema;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

@Controller
@Slf4j
@RequiredArgsConstructor
public class SearchController {

    @Autowired private final CrawlerService crawlerService;
    @Autowired private final ElasticService elasticService;
    @Autowired private final SearchRepository searchRepository;
    @Autowired private final ArticleRedisRepository articleRedisRepository;
    @Autowired private final KoreanSentiment koreanSentiment;
    @Autowired private final ElasticCustomService elasticCustomService;
    @Autowired private final ThemaElasticService themaElasticService;

    @PostMapping("/searchInfo")
    public String extract(@RequestParam("searchInfo") String searchInfo) {

        if(searchInfo.isEmpty()) return "redirect:/";

        crawlerService.googleCrawler(searchInfo);

        List<String> articleList = elasticCustomService.findSimilarWords(Indices.ARTICLE_INDEX, searchInfo); // 뉴스 크롤링 정보 가져오기

        Map<String, Integer> analyzeResult = koreanSentiment.articleAnalyze(articleList); // 검색 테마 감정 분석

        log.info("{}의 긍정 수치 : {}, 부정 수치 : {}", searchInfo, analyzeResult.getOrDefault(Indices.POSITIVE, 0), analyzeResult.getOrDefault(Indices.NEGATIVE, 0));

        List<Thema> themaList = elasticCustomService.findSimilarThema(Indices.ARTICLE_THEMA_INDEX, searchInfo); // 테마 크롤링 정보 전부 가져오기

        for (Thema i : themaList) {
            log.info("테마 명 : {}", i.getThemaName());
            log.info("테마 퍼센트 : {}", i.getPercent());
            log.info("테마 주도주1 : {}", i.getFirstStock());
            log.info("테마 주도주2 : {}", i.getSecondStock());
            log.info("테마 링크 : {}", i.getDetailLink());
        }
//        Map<String, String> themaBestStock = crawlerService.paxNetReadThema(themaList); // 주도주 2개 가져오기

//        log.info("주도주 : {}, {}",themaBestStock.getOrDefault("best1",""), themaBestStock.getOrDefault("best2",""));

        return "redirect:/";
    }
}
