package com.example.elasticsearch.search.controller;

import com.example.elasticsearch.crawler.service.CrawlerService;
import com.example.elasticsearch.elastic.service.ArticleElasticCustomService;
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
import java.util.Optional;

@Controller
@Slf4j
@RequiredArgsConstructor
public class SearchController {

    @Autowired private final CrawlerService crawlerService;
    @Autowired private final ElasticService elasticService;
    @Autowired private final SearchRepository searchRepository;
    @Autowired private final ArticleRedisRepository articleRedisRepository;
    @Autowired private final KoreanSentiment koreanSentiment;
    @Autowired private final ArticleElasticCustomService articleElasticCustomService;
    @Autowired private final ThemaElasticService themaElasticService;

    @PostMapping("/searchInfo")
    public String extract(@RequestParam("searchInfo") String searchInfo) {

        if(searchInfo.isEmpty()) return "redirect:/";

        crawlerService.googleCrawler(searchInfo);

        List<String> list = articleElasticCustomService.findSimilarWords(searchInfo);

        Map<String, Integer> analyzeResult = koreanSentiment.articleAnalyze(list);

        log.info("{}의 긍정 수치 : {}, 부정 수치 : {}", searchInfo, analyzeResult.getOrDefault(Indices.POSITIVE, 0), analyzeResult.getOrDefault(Indices.NEGATIVE, 0));

        Optional<Thema> themaResult = themaElasticService.findByKeyword(searchInfo);

        if(themaResult.isPresent()) log.info("{}의 퍼센트: {}",themaResult.get().getThemaName(), themaResult.get().getPercent());
        if(!themaResult.isPresent()) log.info("해당 테마명은 없습니다.");

        crawlerService.paxNetReadThema(searchInfo);

        return "redirect:/";
    }
}
