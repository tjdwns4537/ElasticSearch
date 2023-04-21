package com.example.elasticsearch.crawler.service;

import com.example.elasticsearch.crawler.repository.StockJpaRepository;
import com.example.elasticsearch.crawler.repository.StockListJpaRepository;
import com.example.elasticsearch.redis.repository.RankingRepository;
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
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class CrawlerService {

    @Autowired private final StockJpaRepository stockJpaRepository;
    @Autowired private final RankingRepository rankingRepository;
    @Autowired private final StockListJpaRepository stockListJpaRepository;

    @Value("${crawler.url}")
    String url;

    @Value("${crawler.findUrl}")
    String findUrl;

    public void crawlerSelectImp() {
        Optional<StockDbDto> saveStock;

        List<StockLikeDto> stockLikeList = stockListJpaRepository.findAll();

        if(stockLikeList.size() > 6){ // 최대 개수 6개로 조정
            stockLikeList = stockLikeList.subList(0, 6);
        }

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
                Elements percentElements = doc.getElementsByAttributeValue("class", "no_exday");
                Element percnetElement = percentElements.get(0);
                Elements percentSpanElements = percnetElement.select(".blind");
                String percentResult = String.format("%.2f",Double.parseDouble(percentSpanElements.get(1).text()));

                /** 거래량 **/
                Elements tradeElements = doc.getElementsByAttributeValue("class", "no_info");
                Element tradeElement = tradeElements.get(0);
                String tradeResult = tradeElement.select(".blind").get(3).text();

                saveStock = Optional.of(stockJpaRepository.save(StockDbDto.of(titleResult, priceResult, percentResult, tradeResult)));
                rankingRepository.setStockRanking(saveStock.get()); // redis 에 저장
            }

        } catch (IOException e) {
            log.error("크롤링에 에러가 발생했습니다.");
        }
    }

    public String findStockNumber(String name) {
        String titleResult = "";
        String selectUrl = findUrl + name;
        log.info("입력 url: {}", selectUrl);
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

    public void saveStackNumber(String titleResult ) {
        StockLikeDto stockLikeDto = StockLikeDto.of(titleResult);
        stockListJpaRepository.save(stockLikeDto);
    }
}
