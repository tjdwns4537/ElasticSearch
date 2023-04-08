package com.example.elasticsearch.crawler;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class CrawlerServiceTest {

    @Test
    @DisplayName("크롤링 정상 작동 테스트")
    public void crawlerImp() {

        /**
         *  TODO
         *      - 삼성전자 ( 005930 )
         *      - 카카오 ( 035720 )
         *      - 네이버 ( 035420 )
         *      - 기아 ( 000270 )
         *      - LG 화학 ( 051910 )
         *      - SK 이노베이션 ( 096770 )
         * **/

        String[] numberArr = {
                "005930", "035720", "035420", "000270", "051910", "096770"
        };

        String url = "https://finance.naver.com/item/main.naver?code=";

        try {

            for (int i = 0; i < numberArr.length; i++) {
                url = url + numberArr[i];
            }

            Document doc = Jsoup.connect(url).get();

            Elements elements = doc.getElementsByAttributeValue("class", "no_today");

            Element element = elements.get(0);

            Elements spanElements = element.select("span");

            System.out.println(spanElements.get(0));
        } catch (IOException e) {

        }
    }

}
