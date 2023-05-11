package com.example.elasticsearch.crawler.controller;

import com.example.elasticsearch.article.domain.Article;
import com.example.elasticsearch.article.service.ArticleService;
import com.example.elasticsearch.redis.repository.ArticleRedisRepository;
import com.example.elasticsearch.search.domain.Search;
import com.example.elasticsearch.search.repository.SearchRepository;
import com.example.elasticsearch.crawler.service.CrawlerService;
import com.example.elasticsearch.elastic.service.ElasticService;
import com.example.elasticsearch.stock.domain.StockForm;
import com.example.elasticsearch.stock.service.StockService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
public class CrawlerController {

    @Autowired private final CrawlerService crawlerService;
    @Autowired private final StockService stockService;
    @Autowired private final ElasticService elasticService;
    @Autowired private final SearchRepository searchRepository;
    @Autowired private final ArticleRedisRepository articleRedisRepository;

    @GetMapping
    public String crawlerService(Model model) {
        crawlerService.likeStockFindAll();
        crawlerService.saveLiveStock();

        List<String> likeStockRanking = stockService.getLikeStockRanking();
        List<String> likeStockAll = stockService.getLikeStockAll();
        List<String> liveStock = stockService.getStockLive();

        model.addAttribute("likeStockRanking",likeStockRanking); // 관심 주식 항목 순위 화면에 출력
        model.addAttribute("likeStockList",likeStockAll); // 관심 주식 항목 화면에 출력
        model.addAttribute("liveStockList",liveStock); // 실시간 주식 순위 화면에 출력
        model.addAttribute("stockForm", new StockForm());
        return "home";
    }

    @GetMapping("/searchInfo")
    public String extract(@RequestParam("searchInfo") String searchInfo) {
        List<String> crawlingArticle = crawlerService.readArticle();
        Search search = Search.of(searchInfo);

        for (String i : crawlingArticle) {
            if(i.contains(searchInfo)) {
                elasticService.stringAnalyze(search, i); // 긍정, 부정 점수 측정
                articleRedisRepository.save(i); // 관련 기사 저장
            }
        }

        searchRepository.save(search);
        crawlerService.readThema(searchInfo);

        return "redirect:/";
    }
}
