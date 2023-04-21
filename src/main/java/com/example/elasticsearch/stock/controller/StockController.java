package com.example.elasticsearch.stock.controller;

import com.example.elasticsearch.crawler.service.CrawlerService;
import com.example.elasticsearch.stock.domain.StockForm;
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

    @PostMapping("/likeStock")
    public String likeList(@ModelAttribute("stockForm") StockForm stockForm, Model model) {
        String stockName = stockForm.getStockName();

        log.info("받아온 데이터: {}", stockName);
        String stockNumber = crawlerService.findStockNumber(stockName); // 주식명으로 주식넘버 조회

        if(stockNumber.isEmpty()){
            log.info("잘못된 StockName입니다. {}", stockName);
            return "page404/notFound";
        }

        log.info("stockNumber : {}",stockNumber);
        crawlerService.saveStackNumber(stockNumber); // DB에 저장
        return "redirect:/";
    }
}
