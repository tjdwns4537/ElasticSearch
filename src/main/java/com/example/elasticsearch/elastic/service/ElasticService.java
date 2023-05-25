package com.example.elasticsearch.elastic.service;

import com.example.elasticsearch.article.domain.ArticleEls;
import com.example.elasticsearch.search.domain.Search;
import com.example.elasticsearch.elastic.repository.ArticleElasticRepository;
import com.example.elasticsearch.helper.Indices;
import com.example.elasticsearch.sentiment.service.KoreanSentiment;
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
}
