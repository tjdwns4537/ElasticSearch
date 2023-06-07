package com.example.elasticsearch.sentiment.service;

import com.example.elasticsearch.article.domain.ArticleVO;
import com.example.elasticsearch.helper.Indices;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class KoreanSentiment {

    public List<ArticleVO> articleAnalyze(List<String> articleList) { // 단어 분석

        List<ArticleVO> artilceList = new ArrayList<>();

        // 로컬 주피터 연결 환경 세팅
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        JSONObject requestBody = new JSONObject();
        String url = "http://localhost:8080/analyze"; // local jupyter url

        for(String i: articleList){
            log.info("입력 기사 : {}", i);
            requestBody.put("text", i);

            HttpEntity<String> requestEntity = new HttpEntity<>(requestBody.toString(), headers);

            // 주피터로 전송
            ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);

            ObjectMapper objectMapper = new ObjectMapper();

            JsonNode jsonNode = null;
            try {
                jsonNode = objectMapper.readTree(responseEntity.getBody());
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }

            String response = jsonNode.get("sentiment").asText();

            log.info("응답 라벨 : {}", response);

            if(response.equals("LABEL_1")) artilceList.add(ArticleVO.of(i, "긍정"));
            if(response.equals("LABEL_0")) artilceList.add(ArticleVO.of(i, "부정"));
        }

        return artilceList;
    }
}
