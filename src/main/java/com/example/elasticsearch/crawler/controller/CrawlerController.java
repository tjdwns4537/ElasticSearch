package com.example.elasticsearch.crawler.controller;

import com.example.elasticsearch.crawler.service.CrawlerService;
import com.example.elasticsearch.elastic.service.StockSearchService;
import com.example.elasticsearch.redis.repository.LikeStockRepository;
import com.example.elasticsearch.redis.repository.LiveStockRepository;
import com.example.elasticsearch.stock.domain.StockDbDto;
import com.example.elasticsearch.stock.domain.StockForm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
@Slf4j
public class CrawlerController {

    @Autowired private final CrawlerService crawlerService;
    @Autowired private final LikeStockRepository likeStockRepository;
    @Autowired private final LiveStockRepository liveStockRepository;
    @Autowired private final StockSearchService stockSearchService;

    @GetMapping
    public String crawlerService(Model model) {
        crawlerService.likeStockFindAll();
        crawlerService.saveLiveStock();

        List<String> likeStockAll = likeStockRepository.getLikeStockAll();
        List<String> likeStockRanking = likeStockRepository.getLikeStockRanking();
        List<String> liveStock = liveStockRepository.getStockLive();

        model.addAttribute("likeStockRanking",likeStockRanking); // 관심 주식 항목 순위 화면에 출력
        model.addAttribute("likeStockList",likeStockAll); // 관심 주식 항목 화면에 출력
        model.addAttribute("liveStockList",liveStock); // 실시간 주식 순위 화면에 출력
        model.addAttribute("stockForm", new StockForm());
        return "home";
    }

    @GetMapping("/stockInfo")
    public String extract(@RequestParam("stockName") String stockName) {
        Optional<StockDbDto> stock = Optional.ofNullable(stockSearchService.findByName(stockName)); // DB에서 해당 종목이름의 데이터 출력
        return "redirect:/";
    }
}
