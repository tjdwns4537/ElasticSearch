package com.example.elasticsearch.crawler;

import com.example.elasticsearch.crawler.repository.StockJpaRepository;
import com.example.elasticsearch.crawler.repository.LikeStockJpaRepository;
import com.example.elasticsearch.stock.domain.StockLikeDto;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.List;

@SpringBootTest
class CrawlerServiceTest {

    @Autowired
    StockJpaRepository stockJpaRepositorys;

    @Autowired
    LikeStockJpaRepository likeStockJpaRepository;

    @Test
    @DisplayName("크롤링 정상 작동 테스트")
    public void crawlerImp() {

        String[] numberArr = {
                "005930", "035720", "035420", "000270", "051910", "096770"
        };
        String url = "https://finance.naver.com/item/main.naver?code=";


        try {
            for (int i = 0; i < numberArr.length; i++) {
                Document doc = Jsoup.connect(url+numberArr[i]).get();

                Elements titleElements = doc.getElementsByAttributeValue("class", "wrap_company");
                Element titleElement = titleElements.get(0);
                Elements title = titleElement.select("a");
                String text1 = title.text();

                Elements priceElements = doc.getElementsByAttributeValue("class", "no_today");
                Element priceElement = priceElements.get(0);
                Elements priceSpanElements = priceElement.select("span");
                String text2 = priceSpanElements.get(0).text();

                String percentText = "";
                Elements percentElements = doc.getElementsByAttributeValue("class", "no_exday");
                Element percnetElement = percentElements.get(0);
                System.out.println(percnetElement);
                Elements percentSpanElements = percnetElement.select(".blind");
                Elements selectDown = percnetElement.select(".no_down");
                Elements selectUp = percnetElement.select(".no_up");
                if(!selectDown.isEmpty()) percentText += "-";
                if(!selectUp.isEmpty()) percentText += "+";
                String percent = percentText + percentSpanElements.get(0).text();

                Elements tradeElements = doc.getElementsByAttributeValue("class", "no_info");
                Element tradeElement = tradeElements.get(0);
                Element trade = tradeElement.select(".blind").get(3);
                String text4 = trade.text();

                System.out.println("title : " + text1);
                System.out.println("price : " + text2);
                System.out.println("percent : " + percent);
                System.out.println("tradeCount : " + text4);

            }

        } catch (IOException e) {

        }
    }


    @DisplayName("종목번호 찾기")
    @Test
    void testFind() {
        String[] arr = {"https://kr.investing.com/search/?q=삼성전자",
                "https://kr.investing.com/search/?q=기아",
                "https://kr.investing.com/search/?q=네이버"
        };

        try{

            for (String i : arr) {
                Document doc = Jsoup.connect(i).get();

                /** 종목 이름 **/
                Elements titleElements = doc.getElementsByAttributeValue("class", "js-inner-all-results-quote-item row");
                Element titleElement = titleElements.get(0);
                Elements title = titleElement.select(".second");
                String titleResult = title.get(0).text();
                StockLikeDto stockLikeDto = StockLikeDto.of(titleResult);
                likeStockJpaRepository.save(stockLikeDto);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        List<StockLikeDto> all = likeStockJpaRepository.findAll();
        for (StockLikeDto i : all) {
            System.out.println("stock : "+i.getLikeStock());
        }
    }

    @Test
    @DisplayName("실시간 차트 종목 순위")
    void liveStock(){
        String url = "https://finance.naver.com/";

        try{
            Document doc = Jsoup.connect(url).get();

            /** 종목 이름 **/
            Elements titleElements = doc.getElementsByAttributeValue("class", "group_type is_active");
            Element titleElement = titleElements.get(0);
            Elements title = titleElement.select("#_topItems1");
            String titleResult = title.get(0).text();
            System.out.println("titleResult : ");
            System.out.println(titleResult);

            String[] splitResult = titleResult.split("% ");
            for (int i = 0; i < splitResult.length; i++) {
                splitResult[i] = splitResult[i] + "%";
                System.out.println("res : " + splitResult[i]);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
