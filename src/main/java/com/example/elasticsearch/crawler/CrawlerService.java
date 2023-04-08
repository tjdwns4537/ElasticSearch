package com.example.elasticsearch.crawler;

import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@Slf4j
public class CrawlerService {

    public void crawlerImp() {

        String url = "https://finance.naver.com/item/main.naver?code=005930";

        try {
            Document doc = Jsoup.connect(url).get();

            System.out.println(doc);

//            Elements elements = doc.getElementsByAttributeValue("class", "list_body section_index");
//
//            Element element = elements.get(0);

        } catch (IOException e) {
            log.error("크롤링에 실패하였습니다. 에러 메세지 : {}",e);
        }

    }
}
