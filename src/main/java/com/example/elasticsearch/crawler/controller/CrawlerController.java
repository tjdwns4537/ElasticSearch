package com.example.elasticsearch.crawler.controller;

import com.example.elasticsearch.crawler.service.CrawlerService;
import com.example.elasticsearch.elastic.service.StockSearchService;
import com.example.elasticsearch.redis.repository.RankingRepository;
import com.example.elasticsearch.stock.domain.StockDbDto;
import com.example.elasticsearch.stock.domain.StockElasticDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
@Slf4j
public class CrawlerController {

    @Autowired private final CrawlerService crawlerService;
    @Autowired private final RankingRepository rankingRepository;
    @Autowired private final StockSearchService stockSearchService;

    @GetMapping
    public String crawlerService(Model model) {
        crawlerService.crawlerImp();
        List<String> stockRanking = rankingRepository.getStockRanking();
        model.addAttribute("stockList",stockRanking); // 실시간 순위 화면에 출력
        return "home";
    }

    @GetMapping("/stockInfo")
    public String extract(@RequestParam("stockName") String stockName) {

        log.info("주식 종목 명 : {}",stockName);
        Optional<StockDbDto> stock = Optional.ofNullable(stockSearchService.findByName(stockName)); // DB에서 해당 종목이름의 데이터 출력

        log.info(stock.get().getStockName());
        log.info(stock.get().getStockPercent());
        log.info(stock.get().getStockPrice());
        log.info(stock.get().getTradeCount());

        return "redirect:/";
    }
}
