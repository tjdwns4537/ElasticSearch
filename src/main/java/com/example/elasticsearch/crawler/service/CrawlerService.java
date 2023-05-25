package com.example.elasticsearch.crawler.service;

import com.example.elasticsearch.article.domain.ArticleEls;
import com.example.elasticsearch.crawler.repository.StockJpaRepository;
import com.example.elasticsearch.crawler.repository.LikeStockJpaRepository;
import com.example.elasticsearch.elastic.repository.ArticleElasticRepository;
import com.example.elasticsearch.elastic.service.ElasticService;
import com.example.elasticsearch.redis.repository.LikeStockRepository;
import com.example.elasticsearch.redis.repository.LiveStockRepository;
import com.example.elasticsearch.stock.domain.StockDbDto;
import com.example.elasticsearch.stock.domain.StockLikeDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class CrawlerService {

    @Autowired private final StockJpaRepository stockJpaRepository;
    @Autowired private final LikeStockRepository likeStockRepository;
    @Autowired private final LiveStockRepository liveStockRepository;
    @Autowired private final LikeStockJpaRepository likeStockJpaRepository;
    @Autowired private final ElasticService elasticService;
    @Autowired private final ArticleElasticRepository articleElasticRepository;

    @Value("${crawler.url}")
    String url;

    @Value("${crawler.findUrl}")
    String findUrl;

    @Value("${crawler.liveUrl}")
    String liveUrl;

    @Value("${crawler.liveArticleUrl}")
    String articleUrl;

    @Value("${crawler.naverThemaUrl}")
    String naverUrl;

    @Value("${crawler.paxNetThemaUrl}")
    String paxNet;

    public void likeStockFindAll() {
        Optional<StockDbDto> saveStock = Optional.empty();
        List<StockLikeDto> stockLikeList = likeStockJpaRepository.findAll();

        try {
            for (StockLikeDto i : stockLikeList) {

                Document doc = Jsoup.connect(url+i.getLikeStock()).get();

                /** 종목 이름 **/
                Elements titleElements = doc.getElementsByAttributeValue("class", "wrap_company");
                Element titleElement = titleElements.get(0);
                Elements title = titleElement.select("a");
                String titleResult = title.get(0).text();

                /** 주식 가격 **/
                Elements priceElements = doc.getElementsByAttributeValue("class", "no_today");
                Element priceElement = priceElements.get(0);
                Elements priceSpanElements = priceElement.select("span");
                String priceResult = priceSpanElements.get(0).text();

                /** 주식 등락율 **/
                StringBuilder percent = new StringBuilder();
                Elements percentElements = doc.getElementsByAttributeValue("class", "no_exday");
                Element percnetElement = percentElements.get(0);
                Elements selectDown = percnetElement.select(".no_down");
                Elements selectUp = percnetElement.select(".no_up");
                if(!selectDown.isEmpty()) percent.append("-");
                if(!selectUp.isEmpty()) percent.append("+");
                Elements percentSpanElements = percnetElement.select(".blind");
                percent.append(String.format("%.2f", Double.parseDouble(percentSpanElements.get(1).text())));

                /** 거래량 **/
                Elements tradeElements = doc.getElementsByAttributeValue("class", "no_info");
                Element tradeElement = tradeElements.get(0);
                String tradeResult = tradeElement.select(".blind").get(3).text();

                StockDbDto stock = StockDbDto.of(titleResult, priceResult, percent.toString(), tradeResult);

                /**
                 * TODO
                 *  - Update 쿼리로 수정 필요
                 * **/
                try {
                    StockDbDto findStock = stockJpaRepository.findByStockName(titleResult);
                    stockJpaRepository.deleteById(findStock.getId());
                    saveStock = Optional.of(stockJpaRepository.save(stock));
                } catch (NullPointerException e) {
                    saveStock = Optional.of(stockJpaRepository.save(stock));
                }

                likeStockRepository.setStockRanking(saveStock.get()); // redis 에 저장
            }

        } catch (IOException e) {
            log.error("크롤링에 에러가 발생했습니다.");
        }
    }

    public String findStockNumber(String name) {
        String titleResult = "";
        String selectUrl = findUrl + name;
        try{
            Document doc = Jsoup.connect(selectUrl).get();

            /** 종목 이름 **/
            Elements titleElements = doc.getElementsByAttributeValue("class", "js-inner-all-results-quote-item row");
            Element titleElement = titleElements.get(0);
            Elements title = titleElement.select(".second");
            titleResult = title.get(0).text();
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
        return titleResult;
    }

    public void saveLiveStock() {
        try{
            Document doc = Jsoup.connect(liveUrl).get();

            /** 종목 이름 **/
            Elements titleElements = doc.getElementsByAttributeValue("class", "group_type is_active");
            Element titleElement = titleElements.get(0);
            Elements title = titleElement.select("#_topItems1");
            String titleResult = title.get(0).text();

            String[] splitResult = titleResult.split("% ");
            for (int i = 0; i < splitResult.length; i++) {
                liveStockRepository.setStockLive(splitResult[i] + "%");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void readArticle() {
        try {
            Document articleDoc = Jsoup.connect(articleUrl).get();

            /** 뉴스기사 **/
            Elements articleTitleElements = articleDoc.getElementsByAttributeValue("class", "cjs_dept_desc");
            Elements articleContentElements = articleDoc.getElementsByAttributeValue("class", "cjs_d");
            List<String> crawlingArticle = articleTitleElements.eachText(); // save in list

            for (String i : crawlingArticle) {
                ArticleEls article = ArticleEls.of(i);
                ArticleEls save = elasticService.save(article);
                log.info("Data: {}", save.getTitle());
                log.info("Data: {}", save.getId());
            }

//            for (String i : crawlingArticle) {
//                // Check if the article already exists in Elasticsearch before saving
//                List<ArticleEls> existingArticles = articleElasticRepository.findByTitleContaining(i);
//                if (existingArticles.isEmpty()) {
//                    ArticleEls article = ArticleEls.of(i);
//                    ArticleEls save = elasticService.save(article);
//                    log.info("Data: {}", save.getTitle());
//                    log.info("Data: {}", save.getId());
//                }
//            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Map<String, String> readThema(String searchInfo) {
        Map<String, String> result = new HashMap<>();

        try{
            Document naverDoc = Jsoup.connect(naverUrl).get();
            Document paxNetDoc = Jsoup.connect(paxNet).get();

            /** 네이버 종목 테마 **/
            Elements naverPercent = naverDoc.getElementsByAttributeValue("class","number col_type2");
            Element naverTitleElement = naverDoc.getElementsByAttributeValue("class", "type_1 theme").get(0);

            Elements naverTitle = naverTitleElement.select(".col_type1");

            String[] naverStockThema = naverTitle.text().split(" "); // 테마명
            String[] naverStockPercent = naverPercent.text().split(" "); // 테마 퍼센트

            for (int i = 0; i < naverStockThema.length; i++) {
                if (naverStockThema[i].equals(searchInfo)){
                    result.put("THEMA_NAME", naverStockThema[i]);
                    result.put("THEMA_PERCENT", naverStockPercent[i]);
                    return result;
                }
            }
//            /** paxNet 종목 테마 **/
//            Elements paxNetTitleElements = paxNetDoc.getElementsByAttributeValue("class", "table-data");
//            Elements paxNetSelect = paxNetTitleElements.select(".ellipsis");
//            String paxNetSelectText = paxNetSelect.text();
//            Elements paxNetSelectPercent = paxNetTitleElements.select(".red");
//
//            String[] paxNetSelectStockThemaName = paxNetSelectText.split(" "); // 테마명
//            String[] percentText = paxNetSelectPercent.text().split(" "); // 테마 퍼센트
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
}
