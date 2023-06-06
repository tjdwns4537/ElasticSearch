package com.example.elasticsearch.search.controller;

import com.example.elasticsearch.crawler.service.CrawlerService;
import com.example.elasticsearch.elastic.service.ElasticCustomService;
import com.example.elasticsearch.elastic.service.ElasticService;
import com.example.elasticsearch.helper.Indices;
import com.example.elasticsearch.redis.redisson.RedissonService;
import com.example.elasticsearch.redis.repository.ArticleRedisRepository;
import com.example.elasticsearch.search.domain.AnalyzeResultSearch;
import com.example.elasticsearch.search.repository.SearchRepository;
import com.example.elasticsearch.sentiment.service.KoreanSentiment;
import com.example.elasticsearch.stock.domain.StockElasticDto;
import com.example.elasticsearch.thema.domain.Thema;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
@Slf4j
@RequiredArgsConstructor
public class SearchController {

    @Autowired
    private final CrawlerService crawlerService;
    @Autowired
    private final ElasticService elasticService;
    @Autowired
    private final SearchRepository searchRepository;
    @Autowired
    private final ArticleRedisRepository articleRedisRepository;
    @Autowired
    private final KoreanSentiment koreanSentiment;
    @Autowired
    private final ElasticCustomService elasticCustomService;
    @Autowired
    private final RedissonService redissonService;

    @GetMapping("/searchResult")
    public String redirectView(@RequestParam("searchInfo") String searchInfo,
                               @RequestParam("articleList") List<String> articleList,
                               @RequestParam("positive") Integer positive,
                               @RequestParam("negative") Integer negative,
//                               @RequestParam("themaList") List<Thema> themaList,
//                               @RequestParam("relateStockList") List<StockElasticDto> relateStockList,
                               Model model) {

        model.addAttribute("searchInfo", searchInfo);
        model.addAttribute("articleList", articleList);
        model.addAttribute("sentimentPositive", positive);
        model.addAttribute("sentimentNegative", negative);
//        model.addAttribute("themaList", themaList);
//        model.addAttribute("relateStockList", relateStockList);

        String redirectUrl = "result-page/result-page"; // The URL to redirect to
        return redirectUrl;
    }

    @PostMapping("/searchInfo")
    public String extract(@RequestParam("searchInfo") String searchInfo, RedirectAttributes redirectAttributes) {

        if (searchInfo.isEmpty()) {
            return "redirect:/";
        }
        boolean lockAcquired = false;
        List<StockElasticDto> relateStockList = new ArrayList<>();
        crawlerService.googleCrawler(searchInfo);

        try {
            List<Thema> themaList = elasticCustomService.findSimilarThema(Indices.THEMA_INDEX, searchInfo);

            if (themaList.isEmpty()) {
                lockAcquired = redissonService.searchLock(searchInfo);
                if (!lockAcquired) {
                    log.info("Failed to acquire lock for searchInfo: {}", searchInfo);
                    return "redirect:/";  // or any other appropriate action
                }
            }

            List<String> articleList = elasticCustomService.findSimilarWords(Indices.ARTICLE_INDEX, searchInfo); // 뉴스 크롤링 정보 가져오기

            Map<String, Integer> analyzeResult = koreanSentiment.articleAnalyze(articleList); // 검색 테마 감정 분석

            log.info("{}의 긍정 수치 : {}, 부정 수치 : {}", searchInfo, analyzeResult.getOrDefault(Indices.POSITIVE, 0), analyzeResult.getOrDefault(Indices.NEGATIVE, 0));

            Integer positive = analyzeResult.getOrDefault(Indices.POSITIVE, 0);
            Integer negative = analyzeResult.getOrDefault(Indices.NEGATIVE, 0);

            for (Thema i : themaList) {
                // Perform logging operations
                log.info("테마 명 : {}", i.getThemaName());
                log.info("테마 퍼센트 : {}", i.getPercent());
                log.info("테마 주도주1 : {}", i.getFirstStock());
                log.info("테마 주도주2 : {}", i.getSecondStock());
                relateStockList = i.getRelateStock();
                for (StockElasticDto j : relateStockList) {
                    log.info("관련 주식 getStockName : {}", j.getStockName());
                    log.info("관련 주식 getPrice : {}", j.getPrice());
                    log.info("관련 주식 getPrevPriceCompare : {}", j.getPrevPriceCompare());
                    log.info("관련 주식 getPrevPriceComparePercent : {}", j.getPrevPriceComparePercent());
                }
            }

//            AnalyzeResultSearch analyzeResultSearch = AnalyzeResultSearch.of(searchInfo, articleList, positive, negative, themaList, relateStockList);

            redirectAttributes.addAttribute("searchInfo", searchInfo);
            redirectAttributes.addAttribute("articleList", articleList);
            redirectAttributes.addAttribute("positive", positive);
            redirectAttributes.addAttribute("negative", negative);
//            redirectAttributes.addAttribute("themaList", themaList);
//            redirectAttributes.addAttribute("relateStockList", relateStockList);

            return "redirect:/searchResult";
        } finally {
            if (lockAcquired) {
                redissonService.searchUnlock(searchInfo);
            }
        }
    }
}
