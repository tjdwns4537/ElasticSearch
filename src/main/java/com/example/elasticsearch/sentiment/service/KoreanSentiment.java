package com.example.elasticsearch.sentiment.service;

import com.example.elasticsearch.helper.Indices;
import com.example.elasticsearch.search.domain.Search;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@Slf4j
public class KoreanSentiment {

    public String labelCheck(String label) {
        if(label.equals("LABEL_1")) return Indices.POSITIVE;
        else return Indices.NEGATIVE;
    }

    public Search articleAnalyze(Search search, String text) { // 단어 분석
        log.info("입력 기사 : {}", text);

        // 로컬 주피터
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        JSONObject requestBody = new JSONObject();
        requestBody.put("text", text);

        HttpEntity<String> requestEntity = new HttpEntity<>(requestBody.toString(), headers);
        String url = "http://localhost:8080/analyze"; // local jupyter url

        // 주피터로 전송
        ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);

        String response = responseEntity.getBody(); // 응답 데이터
        log.info("응답 라벨 : {}", response);

        if(response.equals("LABEL_1")) search.setPositiveNumber(search.getPositiveNumber()+1);
        else search.setNegativeNumber(search.getNegativeNumber()+1);

        return search;
    }
}
