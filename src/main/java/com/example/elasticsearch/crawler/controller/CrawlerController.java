package com.example.elasticsearch.crawler.controller;

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

//    @PostMapping("/googleColab")
//    public void sendData() {
//        RestTemplate restTemplate = new RestTemplate();
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_JSON);
//
//        String message = "Hello, Flask Server!";
//        String url = "http://http://127.0.0.1:5000/analyze"; // google-colab url
//
//        HttpEntity<String> requestEntity = new HttpEntity<>(message, headers);
//        ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);
//
//        String response = responseEntity.getBody();
//    }

    @PostMapping("/searchInfo")
    public String extract(@RequestParam("searchInfo") String searchInfo) {
        List<String> crawlingArticle = crawlerService.readArticle();
        Search search = Search.of(searchInfo);

        for (String i : crawlingArticle) {
            if(i.contains(searchInfo)) {
                search = elasticService.stringAnalyze(search, i);// 주피터를 활용해 긍정, 부정 점수 측정
                articleRedisRepository.save(i); // 관련 기사 저장
                log.info("기사: {}", i);
            }
        }

        searchRepository.save(search); // 검색어 저장 -> 검색어 목록으로 활용

        Map<String, String> themaInfo = crawlerService.readThema(searchInfo); // 검색어 크롤링

        if(themaInfo.isEmpty()){
            log.info("해당하는 테마가 없습니다. 테마명 : {}", searchInfo);
            return "redirect:/";
        }

        log.info("크롤링 검색어 : {}", themaInfo.get("THEMA_NAME"));
        log.info("크롤링 검색어 퍼센트 : {}", themaInfo.get("THEMA_PERCENT"));

        elasticService.readThemaAnalyze(searchInfo);

        return "redirect:/";
    }
}
