package com.example.elasticsearch.crawler;

import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@Slf4j
public class CrawlerService {

    public void crawlerImp() {

        try {
            Document doc = Jsoup.connect("https://news.naver.com/main/read.nhn?mode=LSD&mid=shm&sid1=105&oid=421&aid=0004721371").get();

            Elements elements = doc.select("#articleBodyContents");

            String[] p = elements.get(0).text().split("\\.");
        } catch (IOException e) {
            log.error("크롤링에 실패하였습니다. 에러 메세지 : {}",e);
        }

    }
}
