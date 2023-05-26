package com.example.elasticsearch.crawler.controller;

import com.example.elasticsearch.redis.repository.ArticleRedisRepository;
import com.example.elasticsearch.search.domain.Search;
import com.example.elasticsearch.search.repository.SearchRepository;
import com.example.elasticsearch.crawler.service.CrawlerService;
import com.example.elasticsearch.elastic.service.ElasticService;
import com.example.elasticsearch.sentiment.service.KoreanSentiment;
import com.example.elasticsearch.stock.domain.StockForm;
import com.example.elasticsearch.stock.service.StockService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

import org.json.JSONObject;
import org.json.JSONArray;

@Controller
@RequiredArgsConstructor
@Slf4j
public class CrawlerController {

    @Autowired private final CrawlerService crawlerService;
    @Autowired private final StockService stockService;

    @GetMapping
    public String crawlerService(Model model) {
        crawlerService.likeStockFindAll();
        crawlerService.saveLiveStock();
        crawlerService.readArticle(); // 뉴스 기사 크롤링 수행 -> ELS doc으로 인덱싱

        List<String> likeStockRanking = stockService.getLikeStockRanking();
        List<String> likeStockAll = stockService.getLikeStockAll();
        List<String> liveStock = stockService.getStockLive();

        model.addAttribute("likeStockRanking",likeStockRanking); // 관심 주식 항목 순위 화면에 출력
        model.addAttribute("likeStockList",likeStockAll); // 관심 주식 항목 화면에 출력
        model.addAttribute("liveStockList",liveStock); // 실시간 주식 순위 화면에 출력
        model.addAttribute("stockForm", new StockForm());
        return "home";
    }
}
