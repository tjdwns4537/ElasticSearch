package com.example.elasticsearch.elastic.service;

import com.example.elasticsearch.article.domain.Article;
import com.example.elasticsearch.article.domain.Search;
import com.example.elasticsearch.article.repository.SearchRepository;
import com.example.elasticsearch.elastic.repository.ArticleElasticRepository;
import com.example.elasticsearch.helper.Indices;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.AnalyzeRequest;
import org.elasticsearch.client.indices.AnalyzeResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ArticleSearchService {

    @Autowired
    private final RestHighLevelClient client;

    @Autowired
    private final ArticleElasticRepository articleElasticRepository;

    public Article save(Article article) {
        log.info("출력 : {}", article.getId());
        log.info("출력 : {}", article.getTitle());
        return articleElasticRepository.save(article);
    }

    public List<Article> findByTitle(String title) {
        return articleElasticRepository.findByTitle(title);
    }

    public void senseAnalyze(String text) {
//        GetIndexRequest getIndexRequest = new GetIndexRequest(Indices.ARTICLE_INDEX);
//        boolean exists;

//        try{
//            exists = client.indices().exists(getIndexRequest, RequestOptions.DEFAULT); // 인덱스가 이미 존재하면 기능 수행

//            if (client.indices().exists(getIndexRequest,RequestOptions.DEFAULT)) {
//                DeleteIndexRequest deleteIndexRequest = new DeleteIndexRequest(Indices.ARTICLE_INDEX);
//                client.indices().delete(deleteIndexRequest, RequestOptions.DEFAULT);
//                CreateIndexRequest createIndexRequest = new CreateIndexRequest(Indices.ARTICLE_INDEX);
//                client.indices().create(createIndexRequest, RequestOptions.DEFAULT);
//            }

//            CreateIndexRequest createIndexRequest = new CreateIndexRequest(Indices.ARTICLE_INDEX);
//            client.indices().create(createIndexRequest, RequestOptions.DEFAULT);
//
//            AnalyzeRequest analyzeRequest = AnalyzeRequest.withIndexAnalyzer(Indices.ARTICLE_INDEX, "standard", text);
//            AnalyzeResponse response = client.indices().analyze(analyzeRequest, RequestOptions.DEFAULT);

//            Map<String, Integer> wordCountMap = new HashMap<>();
//            int positiveCount = 0;
//            int negativeCount = 0;
//            for (AnalyzeResponse.AnalyzeToken token : response.getTokens()) {
//                String term = token.getTerm();
//                log.info("term : {}",term);
//                if (isPositive(term)) {
//                    positiveCount++;
//                } else if (isNegative(term)) {
//                    negativeCount++;
//                }
//                int count = wordCountMap.getOrDefault(term, 0);
//                wordCountMap.put(term, count + 1);
//            }

//            log.info("긍정적인 단어 개수 : {}", positiveCount);
//            log.info("부정적인 단어 개수 : {}", negativeCount);
////            log.info("분석된 단어 : {}", wordCountMap);
//
//            DeleteIndexRequest deleteIndexRequest = new DeleteIndexRequest(Indices.ARTICLE_INDEX);
//            client.indices().delete(deleteIndexRequest, RequestOptions.DEFAULT);
//
//            client.close();
//        } catch (IOException e){
//
//        }
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

    public List<String> findAll() {
        List<String> list = new ArrayList<>();
        Iterable<Article> all = articleElasticRepository.findAll();
        for (Article i : all) {
            list.add(i.getTitle());
        }
        return list;
    }

    public void stringAnalyze(Search search, String text) { // 단어 분석
        try{
            AnalyzeRequest analyzeRequest = AnalyzeRequest.withIndexAnalyzer(Indices.ARTICLE_INDEX, "standard", text);

//            AnalyzeRequest request = AnalyzeRequest.withGlobalAnalyzer("nori", text);
            AnalyzeResponse response = client.indices().analyze(analyzeRequest, RequestOptions.DEFAULT);

            List<AnalyzeResponse.AnalyzeToken> tokens = response.getTokens();

            int positiveCount = 0;
            int negativeCount = 0;

            for (AnalyzeResponse.AnalyzeToken token : tokens) {
                String term = token.getTerm();

                log.info("term : {}",term);
                if (isPositive(term)) {
                    positiveCount++;
                } else if (isNegative(term)) {
                    negativeCount++;
                }
            }

            log.info("긍정적인 단어 개수 : {}", positiveCount);
            log.info("부정적인 단어 개수 : {}", negativeCount);
            search.setPositiveNumber(search.getPositiveNumber()+positiveCount);
            search.setNegativeNumber(search.getNegativeNumber()+negativeCount);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
