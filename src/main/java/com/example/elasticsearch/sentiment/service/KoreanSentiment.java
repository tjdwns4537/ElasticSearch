package com.example.elasticsearch.sentiment.service;

import com.example.elasticsearch.article.domain.ArticleEls;
import com.example.elasticsearch.helper.Indices;
import com.example.elasticsearch.search.domain.Search;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
@Slf4j
public class KoreanSentiment {

    public String labelCheck(String label) {
        if(label.equals("LABEL_1")) return Indices.POSITIVE;
        else return Indices.NEGATIVE;
    }

    public String articleAnalyze(List<ArticleEls> articleList) { // 단어 분석

        // 로컬 주피터 연결 환경 세팅
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        JSONObject requestBody = new JSONObject();
        HttpEntity<String> requestEntity = new HttpEntity<>(requestBody.toString(), headers);
        String url = "http://localhost:8080/analyze"; // local jupyter url

        int positive = 0;
        int negative = 0;

        for(ArticleEls i: articleList){
            log.info("입력 기사 : {}", i);
            requestBody.put("text", i);

            // 주피터로 전송
            ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);
            String response = responseEntity.getBody(); // 응답 데이터
            log.info("응답 라벨 : {}", response);

            if(response.equals("LABEL_1")) positive++;
            if(response.equals("LABEL_0")) negative++;
        }

        if(positive > negative) return "P";
        return "N";
    }
}
