package com.example.elasticsearch.crawler;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class CrawlerServiceTest {

    @Test
    @DisplayName("크롤링 정상 작동 테스트")
    public void crawlerImp() {
        String url = "https://finance.naver.com/item/main.naver?code=005930";

        try {
            Document doc = Jsoup.connect(url).get();

            System.out.println(doc);
        } catch (IOException e) {

        }
    }

}
