package com.example.elasticsearch.stock.controller;

import com.example.elasticsearch.crawler.repository.LikeStockJpaRepository;
import com.example.elasticsearch.crawler.repository.StockJpaRepository;
import com.example.elasticsearch.crawler.service.CrawlerService;
import com.example.elasticsearch.stock.domain.StockDbDto;
import com.example.elasticsearch.stock.domain.StockForm;
import com.example.elasticsearch.stock.domain.StockLikeDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@Slf4j
public class StockController {

    @Autowired
    private final CrawlerService crawlerService;
    @Autowired
    private final StockJpaRepository stockJpaRepository;
    @Autowired
    private final LikeStockJpaRepository likeStockJpaRepository;

    @PostMapping("/likeStock")
    public String likeList(@ModelAttribute("stockForm") StockForm stockForm) {
        String stockName = stockForm.getStockName();

        String stockNumber = crawlerService.findStockNumber(stockName); // 주식명으로 주식넘버 조회
        if (stockNumber.isEmpty()) {
            log.info("잘못된 StockName입니다. {}", stockName);
            return "page404/notFound";
        }

        crawlerService.saveStackNumber(stockNumber); // DB에 저장
        return "redirect:/";
    }

    @PostMapping("/deleteLikeStock")
    public String DeleteList() {
        log.info("동작 확인");
        return "redirect:/";
    }
}
