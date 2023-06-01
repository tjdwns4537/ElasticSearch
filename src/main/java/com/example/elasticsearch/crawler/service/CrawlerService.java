package com.example.elasticsearch.crawler.service;

import com.example.elasticsearch.article.domain.ArticleEls;
import com.example.elasticsearch.crawler.repository.StockJpaRepository;
import com.example.elasticsearch.crawler.repository.LikeStockJpaRepository;
import com.example.elasticsearch.elastic.service.ElasticCustomService;
import com.example.elasticsearch.elastic.service.ElasticService;
import com.example.elasticsearch.elastic.service.ThemaElasticService;
import com.example.elasticsearch.helper.Indices;
import com.example.elasticsearch.redis.repository.LikeStockRepository;
import com.example.elasticsearch.redis.repository.LiveStockRepository;
import com.example.elasticsearch.stock.domain.StockDbDto;
import com.example.elasticsearch.stock.domain.StockLikeDto;
import com.example.elasticsearch.thema.domain.Thema;
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
    @Autowired private final ThemaElasticService themaElasticService;
    @Autowired private final ElasticCustomService elasticCustomService;

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
    String paxNetUrl;

    @Value(("${crawler.naverUpJongUrl}"))
    String naverUpjongUrl;

    public void naverUpjongCrawler() {
        try {
            Document doc = Jsoup.connect(naverUpjongUrl).get();
            Elements big1 = doc.getElementsByAttributeValue("class", "type_1");
            Elements trSelect = big1.get(0).select("tr");

            for (int i = 2; i < trSelect.size(); i++) {
                Element tdSelect = trSelect.get(i).selectFirst("td");
                Elements href = tdSelect.getElementsByAttribute("href");
                Elements percentEl = trSelect.get(i).getElementsByAttributeValue("class","tah p11 red01");

                if(!percentEl.hasText()){ // 상승 또는 하락
                    percentEl = trSelect.get(i).getElementsByAttributeValue("class","tah p11 nv01");
                }

                if (href.hasText() && percentEl.hasText() ) {
                    String upjong = href.text();
                    String percent = percentEl.text();
                    String detailLink = href.attr("href");
                    themaElasticService.themaSave(Thema.of(upjong, percent, detailLink));
                }
            }
        } catch (IOException e) {

        }
    }

    public Map<String, String> paxNetReadThema() {
        Map<String, String> result = new HashMap<>();

        try {
            Document paxNetDoc = Jsoup.connect(paxNetUrl).get();

            Elements divValue = paxNetDoc.getElementsByAttributeValue("class", "table-data");

            Element tbody = divValue.select("tbody").get(1);

            Elements tdValue = tbody.select("td");

            for (int i = 0; i < tdValue.size(); i++) {

                Elements themaNames = tdValue.get(i).getElementsByAttributeValue("class", "ellipsis");
                Elements next = tdValue.get(i).getElementsByAttributeValue("class", "ellipsis").next();

                if (next.hasText() && themaNames.hasText()) {
                    String percent = next.text();
                    String themaName = themaNames.text();
                    String best1 = tdValue.get(i + 6).getElementsByAttribute("href").text();
                    String best2 = tdValue.get(i + 7).getElementsByAttribute("href").text();

                    log.info("paxNet 저장 : {}, {} ,{} ,{}", themaName, percent, best1, best2);

                    Thema thema = Thema.of(themaName, percent, best1, best2);

                    themaElasticService.themaSave(thema);
                }
            }
        }
        catch (IOException e) {
        }
        return result;
    }

    public void googleCrawler(String searchInfo) {
        try {
            for (int i = 0; i < 5; i++) {
                String url = "https://www.google.com/search?q=" + searchInfo + "&tbm=nws&sxsrf=APwXEdf_M3-MhMZ4bGGTwdUIp4Xcy2ZIeg:1685424458306&ei=Sol1ZNivEpDr-Qad-LdY&start=" + i + "0&sa=N&ved=2ahUKEwjY_Iexp5z_AhWQdd4KHR38DQsQ8tMDegQIBBAE&biw=1440&bih=734&dpr=2";
                Document doc = Jsoup.connect(url).get();
                Elements titleElements = doc.getElementsByAttributeValue("class", "n0jPhd ynAwRc MBeuO nDgy9d");
                for (Element j : titleElements) {
                    elasticService.articleSave(ArticleEls.of(j.text()));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

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
//            Elements articleContentElements = articleDoc.getElementsByAttributeValue("class", "cjs_d");
            List<String> crawlingArticle = articleTitleElements.eachText(); // save in list

            for (String i : crawlingArticle) {
                ArticleEls article = ArticleEls.of(i);
                elasticService.articleSave(article);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void naverReadThema() {
        for (int i = 1; i < 8; i++) {
            try{
                Document naverDoc = Jsoup.connect(naverUrl).get();

                /** 네이버 테마 관련 퍼센트를 크롤링 **/
                Elements naverPercent = naverDoc.getElementsByAttributeValue("class","number col_type2");
                Element naverTitleElement = naverDoc.getElementsByAttributeValue("class", "type_1 theme").get(0);
                Elements naverTitle = naverTitleElement.select(".col_type1");

                String[] naverStockPercent = naverPercent.text().split(" "); // 테마 퍼센트

                for (int j=1; j<naverTitle.size(); j++) {
                    Element element = naverTitle.get(j); // thema name
                    String themaName = element.text();
                    String percent = naverStockPercent[j-1]; // thema percent
                    themaElasticService.themaSave(Thema.of(themaName, percent));
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
