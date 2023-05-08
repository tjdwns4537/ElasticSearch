package com.example.elasticsearch.elastic.service;

import com.example.elasticsearch.article.domain.Article;
import com.example.elasticsearch.elastic.repository.ArticleElasticRepository;
import com.example.elasticsearch.helper.Indices;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.bulk.BulkItemResponse;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.AnalyzeRequest;
import org.elasticsearch.client.indices.AnalyzeResponse;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.rest.RestStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchTemplate;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
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
        try {
            DeleteIndexRequest request = new DeleteIndexRequest(Indices.ARTICLE_INDEX);
            client.indices().delete(request, RequestOptions.DEFAULT);
        } catch (ElasticsearchException e) {
            if (e.status() == RestStatus.NOT_FOUND) {
                // The index doesn't exist, so we don't need to delete it
                System.out.println("NOT_FOUND");
            } else {
                throw e;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        CreateIndexRequest createIndexRequest = new CreateIndexRequest(Indices.ARTICLE_INDEX);
        client.indices().create(createIndexRequest, RequestOptions.DEFAULT);

        AnalyzeRequest request = AnalyzeRequest.withIndexAnalyzer(Indices.ARTICLE_INDEX, "standard", "true true false");
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

        log.info("긍정적인 단어 개수 : {}", positiveCount);
        log.info("부정적인 단어 개수 : {}", negativeCount);
        log.info("분석된 단어 : {}", wordCountMap);

        client.close();
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
}
