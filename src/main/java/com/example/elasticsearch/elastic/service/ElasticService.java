package com.example.elasticsearch.elastic.service;

import com.example.elasticsearch.article.domain.ArticleEls;
import com.example.elasticsearch.search.domain.Search;
import com.example.elasticsearch.elastic.repository.ArticleElasticRepository;
import com.example.elasticsearch.helper.Indices;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.AnalyzeRequest;
import org.elasticsearch.client.indices.AnalyzeResponse;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class ElasticService {

    @Autowired
    private final RestHighLevelClient client;

    @Autowired
    private final ArticleElasticRepository articleElasticRepository;

//    public ArticleEls save(ArticleEls articleEls) {
//        log.info("출력 : {}", articleEls.getId());
//        log.info("출력 : {}", articleEls.getTitle());
//        return articleElasticRepository.save(articleEls);
//    }

    public void readThemaAnalyze(String searchInfo) {

        try{
            AnalyzeRequest analyzeRequest = AnalyzeRequest.withIndexAnalyzer(Indices.ARTICLE_THEMA_INDEX, "standard", searchInfo);
            AnalyzeResponse response = client.indices().analyze(analyzeRequest, RequestOptions.DEFAULT);

            Map<String, Integer> wordCountMap = new HashMap<>();
            for (AnalyzeResponse.AnalyzeToken token : response.getTokens()) {
                String term = token.getTerm();
                log.info("term : {}",term);
                int count = wordCountMap.getOrDefault(term, 0);
                wordCountMap.put(term, count + 1);
            }

            log.info("분석된 단어 : {}", wordCountMap);
            client.close();
        } catch (IOException e){

        }
    }

    private boolean isPositive(String word) throws IOException {
        BufferedReader reader = new BufferedReader(
                new FileReader("/Users/parksungjun/Desktop/ElasticSearchProject/ElasticSearch/src/main/resources/static/elastic/positive_words_self.txt")
        );
        return checkWords(reader, word);
    }

    private boolean isNegative(String word) throws IOException {
        BufferedReader reader = new BufferedReader(
                new FileReader("/Users/parksungjun/Desktop/ElasticSearchProject/ElasticSearch/src/main/resources/static/elastic/negative_words_self.txt")
        );
        return checkWords(reader, word);
    }

    private boolean checkWords(BufferedReader reader, String word) throws IOException {
        String str;
        while ((str = reader.readLine()) != null) {
            if(word.equals(str)) return true;
        }
        reader.close();
        return false;
    }

    public Search stringAnalyze(Search search, String text) { // 단어 분석
        log.info("입력 기사 : {}", text);

        // 로컬 주피터
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        JSONObject requestBody = new JSONObject();
        requestBody.put("text", text);

        HttpEntity<String> requestEntity = new HttpEntity<>(requestBody.toString(), headers);
        String url = "http://localhost:8080/process"; // local jupyter url

        ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);

        String response = responseEntity.getBody();
        log.info("response : {}", response);

        search.setNegativeNumber(1);
        search.setPositiveNumber(2);

        return search;

//        try{
//            AnalyzeRequest analyzeRequest = AnalyzeRequest.withIndexAnalyzer(Indices.ARTICLE_INDEX, "standard", text);
//            AnalyzeResponse response = client.indices().analyze(analyzeRequest, RequestOptions.DEFAULT);
//
//            List<AnalyzeResponse.AnalyzeToken> tokens = response.getTokens();
//
//            int positiveCount = 0;
//            int negativeCount = 0;
//
//            for (AnalyzeResponse.AnalyzeToken token : tokens) {
//                String term = token.getTerm();
//
//                if (isPositive(term)) {
//                    positiveCount++;
//                } else if (isNegative(term)) {
//                    negativeCount++;
//                }
//            }
//
//            log.info("긍정적인 단어 개수 : {}", positiveCount);
//            log.info("부정적인 단어 개수 : {}", negativeCount);
//            search.setPositiveNumber(search.getPositiveNumber()+positiveCount);
//            search.setNegativeNumber(search.getNegativeNumber()+negativeCount);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }
}
