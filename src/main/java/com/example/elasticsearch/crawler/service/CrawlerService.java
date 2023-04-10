package com.example.elasticsearch.crawler.service;

import com.example.elasticsearch.helper.StockEnum;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@Slf4j
public class CrawlerService {

    @Value("${crawler.url}")
    String url;

    public void crawlerImp() {

        String[] arr = {
                StockEnum.KAKAO.getNumber(),
                StockEnum.SAMSUNG.getNumber(),
                StockEnum.NAVER.getNumber(),
                StockEnum.SKENOVATION.getNumber(),
                StockEnum.KIA.getNumber(),
                StockEnum.LGCHEMISTRY.getNumber(),
        };

        try {
            for (int i = 0; i < arr.length; i++) {

                Document doc = Jsoup.connect(url+arr[i]).get();

                /** 종목 이름 **/
                Elements titleElements = doc.getElementsByAttributeValue("class", "wrap_company");
                Element titleElement = titleElements.get(0);
                Elements title = titleElement.select("a");
                System.out.println(title.get(0).text());

                /** 주식 가격 **/
                Elements priceElements = doc.getElementsByAttributeValue("class", "no_today");
                Element priceElement = priceElements.get(0);
                Elements priceSpanElements = priceElement.select("span");
                System.out.println(priceSpanElements.get(0).text());

                /** 주식 등락율 **/
                Elements percentElements = doc.getElementsByAttributeValue("class", "no_exday");
                Element percnetElement = percentElements.get(0);
                Elements percentSpanElements = percnetElement.select(".blind");
                System.out.println(percentSpanElements.get(1).text());
            }

        } catch (IOException e) {

        }

    }
}
