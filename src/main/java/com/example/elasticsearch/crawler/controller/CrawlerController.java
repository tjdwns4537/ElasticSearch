package com.example.elasticsearch.crawler.controller;

import com.example.elasticsearch.crawler.service.CrawlerService;
import com.example.elasticsearch.elastic.service.ElasticCustomService;
import com.example.elasticsearch.elastic.service.ThemaElasticService;
import com.example.elasticsearch.helper.Timer;
import com.example.elasticsearch.kafka.service.CrawlingKafkaService;
import com.example.elasticsearch.kafka.service.MainKafkaService;
import com.example.elasticsearch.stock.domain.StockForm;
import com.example.elasticsearch.stock.service.StockService;
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
    @Autowired private final StockService stockService;
    @Autowired private final CrawlingKafkaService crawlingKafkaService;


    @GetMapping
    public String crawlerService(Model model) {
        crawlerService.likeStockFindAll();
        crawlerService.saveLiveStock();
        crawlingKafkaService.sendNaverArticleMessage("기사 크롤링");
        crawlingKafkaService.sendNaverThemaMessage1(1);
        crawlingKafkaService.sendNaverThemaMessage2(2);
        crawlingKafkaService.sendNaverThemaMessage3(3);
        crawlingKafkaService.sendNaverThemaMessage4(4);
        crawlingKafkaService.sendNaverThemaMessage5(5);
        crawlingKafkaService.sendNaverThemaMessage6(6);
        crawlingKafkaService.sendNaverThemaMessage7(7);

//        crawlerService.naverUpjongCrawler(); // 업종 관련 크롤링

        /**
         * TODO
         *  - 테마 관련 정보 아닐 시에 업종 관련 크롤링을 하는 조건문이 필요함
         *      -> 이를 처리하지 않으면 관련 주식 종목을 불러올 때, null 오류를 발생시킴
         * **/
        //        crawlerService.paxNetReadThema(); naver 크롤링에서 다 처리해줘서 기능이 필요없어짐

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
