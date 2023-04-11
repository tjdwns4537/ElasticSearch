package com.example.elasticsearch.crawler.controller;

import com.example.elasticsearch.crawler.service.CrawlerService;
import com.example.elasticsearch.stock.domain.StockDbDto;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Controller
@RequiredArgsConstructor
@Slf4j
public class CrawlerController {

    @Autowired private final CrawlerService crawlerService;

    @GetMapping("/main")
    public String crawlerService(Model model) {
        StockDbDto stockDbDto = crawlerService.crawlerImp();
        model.addAttribute("stock",stockDbDto);

        log.info("insert data : {} ",stockDbDto.getStockName());
        return "home";
    }
}
