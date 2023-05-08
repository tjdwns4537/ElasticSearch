package com.example.elasticsearch.elastic.service;

import com.example.elasticsearch.article.domain.Article;
import com.example.elasticsearch.elastic.repository.ArticleElasticRepository;
import com.example.elasticsearch.helper.Indices;
import lombok.RequiredArgsConstructor;
import org.elasticsearch.action.bulk.BulkItemResponse;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.AnalyzeRequest;
import org.elasticsearch.client.indices.AnalyzeResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchTemplate;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ArticleSearchService {

    @Autowired
    private final RestHighLevelClient client;

    @Autowired
    private final ArticleElasticRepository articleElasticRepository;

    public Article save(Article article) {
        return articleElasticRepository.save(article);
    }

    public List<Article> findByTitle(String title) {
        return articleElasticRepository.findByTitle(title);
    }

    public void senseAnalyze(String text) throws IOException {
        AnalyzeRequest request = AnalyzeRequest.withGlobalAnalyzer(Indices.ARTICLE_INDEX, text);
        AnalyzeResponse response = client.indices().analyze(request, RequestOptions.DEFAULT);

        Map<String, Integer> wordCountMap = new HashMap<>();
        int positiveCount = 0;
        int negativeCount = 0;

        for (AnalyzeResponse.AnalyzeToken token : response.getTokens()) {
            String term = token.getTerm();
            if (isPositive(term)) {
                positiveCount++;
            } else if (isNegative(term)) {
                negativeCount++;
            }
            int count = wordCountMap.getOrDefault(term, 0);
            wordCountMap.put(term, count + 1);
        }

        System.out.println("Positive count: " + positiveCount);
        System.out.println("Negative count: " + negativeCount);
        System.out.println("Word count map: " + wordCountMap);
    }


    public void analysisString(Article article) { // 단어 분석
        try{
            AnalyzeRequest request = AnalyzeRequest.withGlobalAnalyzer("nori", article.getTitle());
            AnalyzeResponse response = client.indices().analyze(request, RequestOptions.DEFAULT);

            List<AnalyzeResponse.AnalyzeToken> tokens = response.getTokens();
            for (AnalyzeResponse.AnalyzeToken token : tokens) {
                String term = token.getTerm();
                System.out.println(term);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void textGrad(Article article) { // 단어 분석
        try{
            AnalyzeRequest request = AnalyzeRequest.withGlobalAnalyzer("nori", article.getTitle());
            AnalyzeResponse response = client.indices().analyze(request, RequestOptions.DEFAULT);

            List<AnalyzeResponse.AnalyzeToken> tokens = response.getTokens();
            for (AnalyzeResponse.AnalyzeToken token : tokens) {
                String term = token.getTerm();
                System.out.println(term);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean isPositive(String word) {
        // Add your logic to check if a word is positive
        // Return true if positive, false otherwise
        // Example: return positiveWords.contains(word);
        return true;
    }

    private boolean isNegative(String word) {
        // Add your logic to check if a word is negative
        // Return true if negative, false otherwise
        // Example: return negativeWords.contains(word);
        return true;
    }
}
