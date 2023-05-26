package com.example.elasticsearch.search.controller;

import com.example.elasticsearch.article.domain.ArticleEls;
import com.example.elasticsearch.crawler.service.CrawlerService;
import com.example.elasticsearch.elastic.service.ElasticService;
import com.example.elasticsearch.helper.Indices;
import com.example.elasticsearch.redis.repository.ArticleRedisRepository;
import com.example.elasticsearch.search.repository.SearchRepository;
import com.example.elasticsearch.sentiment.service.KoreanSentiment;
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

    @PostMapping("/searchInfo")
    public String extract(@RequestParam("searchInfo") String searchInfo) {

        if(searchInfo.isEmpty()) return "redirect:/";

        List<ArticleEls> list = elasticService.ContainByKeyword(searchInfo);

        Map<String, Integer> result = koreanSentiment.articleAnalyze(list);

        elasticService.deleteAll();

        log.info("{}의 긍정 수치 : {}, 부정 수치 : {}", searchInfo, result.get(Indices.POSITIVE), result.get(Indices.NEGATIVE));

//        Map<String, String> themaInfo = crawlerService.readThema(searchInfo); // 검색어 크롤링
//
//        if(themaInfo.isEmpty()){
//            log.info("해당하는 테마가 없습니다. 테마명 : {}", searchInfo);
//            return "redirect:/";
//        }
//
//        log.info("크롤링 검색어 : {}", themaInfo.get("THEMA_NAME"));
//        log.info("크롤링 검색어 퍼센트 : {}", themaInfo.get("THEMA_PERCENT"));
//        elasticService.readThemaAnalyze(searchInfo);
        return "redirect:/";
    }
}
