package com.example.elasticsearch.search.controller;

import com.example.elasticsearch.article.domain.ArticleVO;
import com.example.elasticsearch.crawler.service.CrawlerService;
import com.example.elasticsearch.elastic.service.ElasticCustomService;
import com.example.elasticsearch.helper.Indices;
import com.example.elasticsearch.helper.Timer;
import com.example.elasticsearch.kafka.service.CrawlingKafkaService;
import com.example.elasticsearch.redis.redisson.RedissonService;
import com.example.elasticsearch.redis.repository.RecommendStockRedisRepo;
import com.example.elasticsearch.search.service.SearchService;
import com.example.elasticsearch.sentiment.service.KoreanSentiment;
import com.example.elasticsearch.stock.domain.FinanceStockRedis;
import com.example.elasticsearch.stock.domain.StockElasticDto;
import com.example.elasticsearch.thema.domain.Thema;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;

@Controller
@Slf4j
@RequiredArgsConstructor
public class SearchController {

    @Autowired
    private final CrawlerService crawlerService;
    @Autowired
    private final KoreanSentiment koreanSentiment;
    @Autowired
    private final ElasticCustomService elasticCustomService;
    @Autowired
    private final RedissonService redissonService;
    @Autowired
    private final SearchService searchService;
    @Autowired
    private final RecommendStockRedisRepo recommendStockRedisRepo;

    @GetMapping("/searchResult")
    public String redirectView(@RequestParam("searchInfo") String searchInfo,
                               @ModelAttribute("positiveInfo") String positiveInfo,
                               @ModelAttribute("negativeInfo") String negativeInfo,
                               @ModelAttribute("analyzeResult") ArrayList<ArticleVO> analyzeResult,
                               @ModelAttribute("themaList") ArrayList<Thema> themaList,
                               @ModelAttribute("relateStockList") ArrayList<StockElasticDto> relateStockList,
                               @ModelAttribute("financeStockList") ArrayList<FinanceStockRedis> financeStockList,
                               Model model) {

//        for (FinanceStockRedis i : financeStockList) {
//            log.info("FinanceStockRedis check : {}", i.getStockName());
//            log.info("FinanceStockRedis check : {}", i.getProfit());
//        }

        model.addAttribute("searchInfo", searchInfo);
        model.addAttribute("analyzeResult", analyzeResult);
        model.addAttribute("positiveInfo", positiveInfo);
        model.addAttribute("negativeInfo", negativeInfo);
        model.addAttribute("themaList", themaList);
        model.addAttribute("relateStockList", relateStockList);
        model.addAttribute("potentialList", financeStockList);

        String redirectUrl = "result-page/result-page"; // The URL to redirect to
        return redirectUrl;
    }

    @GetMapping("/searchResult-inc")
    public String redirectViewInc(@RequestParam("searchInfo") String searchInfo,
                               @ModelAttribute("positiveInfo") String positiveInfo,
                               @ModelAttribute("negativeInfo") String negativeInfo,
                               @ModelAttribute("analyzeResult") ArrayList<ArticleVO> analyzeResult,
                               Model model) {

        model.addAttribute("searchInfo", searchInfo);
        model.addAttribute("analyzeResult", analyzeResult);
        model.addAttribute("positiveInfo", positiveInfo);
        model.addAttribute("negativeInfo", negativeInfo);

        String redirectUrl = "result-page/result-page-inc"; // The URL to redirect to
        return redirectUrl;
    }

    @PostMapping("/searchInfo")
    public String extract(@RequestParam("searchInfo") String searchInfo,
                          @ModelAttribute("positiveInfo") String positiveInfo,
                          @ModelAttribute("negativeInfo") String negativeInfo,
                          @ModelAttribute("analyzeResult") ArrayList<ArticleVO> list,
                          @ModelAttribute("themaList") ArrayList<Thema> themaList,
                          @ModelAttribute("relateStockList") ArrayList<StockElasticDto> relateStockList,
                          @ModelAttribute("financeStockList") ArrayList<FinanceStockRedis> financeStockList,
                          RedirectAttributes redirectAttributes) {

        if (searchInfo.isEmpty()) {
            return "redirect:/";
        }

        log.info("검색 시작 : {} keyword, time : {}", searchInfo, Timer.time());

        // 감정 분석 시작
        int positive = 0;
        int negative = 0;

        crawlerService.googleCrawler(searchInfo);
        List<String> articleList = elasticCustomService.findSimilarWords(Indices.ARTICLE_INDEX, searchInfo); // 뉴스 크롤링 정보 가져오기
        List<ArticleVO> analyzeResult = koreanSentiment.articleAnalyze(articleList); // 검색 테마 감정 분석

        for (ArticleVO i : analyzeResult) {
            String label = i.getAnalyzeResult();
            if (label.equals("긍정")) positive++;
            if (label.equals("부정")) negative++;
        }

        positiveInfo = String.valueOf(positive);
        negativeInfo = String.valueOf(negative);

        // 테마 관련 기능 시작
        recommendStockRedisRepo.deleteAll(); // 관련 주식 순위 레디스 저장소 한번 비워주기
        themaList = elasticCustomService.findSimilarThema(Indices.THEMA_INDEX, searchInfo);

        if(themaList.isEmpty()){
            redirectAttributes.addAttribute("searchInfo", searchInfo);
            redirectAttributes.addFlashAttribute("positiveInfo", positiveInfo);
            redirectAttributes.addFlashAttribute("negativeInfo", negativeInfo);
            redirectAttributes.addFlashAttribute("analyzeResult", analyzeResult);
            return "redirect:/searchResult-inc";
        }

        for (int i = 0; i < themaList.size(); i++) {
            relateStockList = themaList.get(i).getRelateStock();
            searchService.saveBestStock(relateStockList);
        }

        financeStockList = searchService.extractBestStock();

        log.info("테마 관련 주식 기능 수행 끝");

        redirectAttributes.addAttribute("searchInfo", searchInfo);
        redirectAttributes.addFlashAttribute("positiveInfo", positiveInfo);
        redirectAttributes.addFlashAttribute("negativeInfo", negativeInfo);
        redirectAttributes.addFlashAttribute("analyzeResult", analyzeResult);
        redirectAttributes.addFlashAttribute("themaList", themaList);
        redirectAttributes.addFlashAttribute("relateStockList", relateStockList);
        redirectAttributes.addFlashAttribute("financeStockList", financeStockList);

        log.info("검색 끝 : {} keyword, time : {}", searchInfo, Timer.time());

        return "redirect:/searchResult";
    }
}
