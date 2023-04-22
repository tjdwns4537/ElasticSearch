package com.example.elasticsearch.stock.controller;

import com.example.elasticsearch.crawler.service.CrawlerService;
import com.example.elasticsearch.stock.domain.StockForm;
import com.example.elasticsearch.stock.service.StockService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@Slf4j
public class StockController {

    @Autowired
    private final StockService stockService;

    @PostMapping("/likeStock")
    public String likeList(@ModelAttribute("stockForm") StockForm stockForm) {
        String stockName = stockForm.getStockName();

        String stockNumber = stockService.saveStockNumber(stockName);// DB에 저장

        if (stockNumber.isEmpty()) {
            log.info("잘못된 StockName입니다. {}", stockName);
            return "page404/notFound";
        }
        return "redirect:/";
    }

    @PostMapping("/deleteLikeStock")
    public String DeleteList(@ModelAttribute("stockForm") StockForm stockForm) {
        String stockName = stockForm.getStockName();
        stockService.deleteLikeStock(stockName);

        return "redirect:/";
    }
}
