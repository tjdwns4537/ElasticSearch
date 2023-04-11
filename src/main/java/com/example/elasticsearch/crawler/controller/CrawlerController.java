package com.example.elasticsearch.crawler.controller;

import com.example.elasticsearch.crawler.service.CrawlerService;
import com.example.elasticsearch.redis.repository.RankingRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
public class CrawlerController {

    @Autowired private final CrawlerService crawlerService;
    @Autowired private final RankingRepository rankingRepository;

    @GetMapping("/main")
    public String crawlerService(Model model) {
        crawlerService.crawlerImp();
        List<String> stockRanking = rankingRepository.getStockRanking();
        model.addAttribute("stockList",stockRanking); // 실시간 순위 화면에 출력
        return "home";
    }
}
