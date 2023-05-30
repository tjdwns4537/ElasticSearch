package com.example.elasticsearch.elastic.service;

import com.example.elasticsearch.helper.Indices;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.AnalyzeRequest;
import org.elasticsearch.client.indices.AnalyzeResponse;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;

@Service
@RequiredArgsConstructor
@Slf4j
public class ElasticCustomService {

    @Autowired
    private final RestHighLevelClient client;

    public void readThemaAnalyze(String searchInfo) {

        try {
            AnalyzeRequest analyzeRequest = AnalyzeRequest.withIndexAnalyzer(Indices.ARTICLE_THEMA_INDEX, "standard", searchInfo);
            AnalyzeResponse response = client.indices().analyze(analyzeRequest, RequestOptions.DEFAULT);

            Map<String, Integer> wordCountMap = new HashMap<>();
            for (AnalyzeResponse.AnalyzeToken token : response.getTokens()) {
                String term = token.getTerm();
                log.info("term : {}", term);
                int count = wordCountMap.getOrDefault(term, 0);
                wordCountMap.put(term, count + 1);
            }

            log.info("분석된 단어 : {}", wordCountMap);
            client.close();
        } catch (IOException e) {

        }
    }

    public List<String> findSimilarWords(String title) {
        try {
            SearchRequest searchRequest = new SearchRequest(Indices.ARTICLE_INDEX);
            SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();

            // Create a BoolQuery to combine multiple queries
            BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();

            // Add a match query for exact match
            boolQueryBuilder.should(QueryBuilders.matchQuery("title", title));

            // Add a wildcard query for similar words
            String wildcardQuery = "*" + title + "*";
            boolQueryBuilder.should(QueryBuilders.wildcardQuery("title", wildcardQuery));

            // Add a space query for simlar words
            String spaceQuery = "* " + title + " *";
            boolQueryBuilder.should(QueryBuilders.wildcardQuery("title", spaceQuery));

            boolQueryBuilder.should(QueryBuilders.matchQuery("title", title).fuzziness("AUTO"));

            sourceBuilder.query(boolQueryBuilder);
            searchRequest.source(sourceBuilder);

            SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
            SearchHits searchHits = searchResponse.getHits();

            List<String> similarWords = new ArrayList<>();
            for (SearchHit hit : searchHits.getHits()) {
                String similarTitle = hit.getSourceAsMap().get("title").toString();
                similarWords.add(similarTitle);
            }

            client.close();

            return similarWords;
        } catch (IOException e) {
            // Handle exception
            return new ArrayList<>();
        }
    }
}
