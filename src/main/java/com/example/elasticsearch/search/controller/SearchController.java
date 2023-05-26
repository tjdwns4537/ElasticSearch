package com.example.elasticsearch.search.controller;

import com.example.elasticsearch.article.domain.ArticleEls;
import com.example.elasticsearch.crawler.service.CrawlerService;
import com.example.elasticsearch.elastic.service.ElasticService;
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

        elasticService.deleteAll();

        //        Search search = Search.of(searchInfo);

//        for (String i : crawlingArticle) {
//            if(i.contains(searchInfo)) {
//                search = koreanSentiment.articleAnalyze(search, i);// 주피터를 활용해 긍정, 부정 점수 측정
//                articleRedisRepository.save(i); // 관련 기사 저장
//                log.info("기사: {}", i);
//            }
//        }
//
//        searchRepository.save(search); // 검색어 저장 -> 검색어 목록으로 활용

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
