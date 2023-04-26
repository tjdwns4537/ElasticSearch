package com.example.elasticsearch.elastic.service;

import com.example.elasticsearch.config.ElasticSearchConfig;
import com.example.elasticsearch.elastic.repository.ArticleElasticRepository;
import com.example.elasticsearch.helper.Indices;
import com.example.elasticsearch.article.domain.Article;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.AnalyzeRequest;
import org.elasticsearch.client.indices.AnalyzeResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.IndexQuery;
import org.springframework.data.elasticsearch.core.query.IndexQueryBuilder;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class ArticleSearchServiceTest {

    @Autowired
    private ElasticsearchRestTemplate elasticsearchRestTemplate;

    @Autowired
    private ArticleElasticRepository articleElasticRepository;

    @Autowired
    private RestHighLevelClient client;


    @Test
    @DisplayName("Elastic Search Teample save TEST")
    public void saveTemplate() throws JsonProcessingException {
        Article article = new Article("1", "I like USA's tech");

        IndexQuery indexQuery = new IndexQueryBuilder()
                .withId(article.getId().toString())
                .withObject(article)
                .build();

        IndexCoordinates indexCoordinates = IndexCoordinates.of(Indices.ARTICLE_INDEX);

        String documentId = elasticsearchRestTemplate.index(indexQuery,indexCoordinates);
        assertThat(documentId).isNotNull();

        Article retrievedArticle = elasticsearchRestTemplate.get(article.getId(), Article.class);
        assertThat(retrievedArticle).isNotNull();
        String id = retrievedArticle.getId();
        String title = retrievedArticle.getTitle();
        assertThat(id).isEqualTo(article.getId());
        assertThat(title).isEqualTo(article.getTitle());

        System.out.println("save id : "+id);
        System.out.println("save title : "+title);
    }

    @Test
    @DisplayName("ElasticSearch Repository Test")
    public void saveRepository() {
        Article article = Article.of("I like USA's tech");
        Article savedArticle = articleElasticRepository.save(article);

        Optional<Article> foundArticle = articleElasticRepository.findById(savedArticle.getId());

        assertThat(foundArticle).isPresent();
        assertThat(foundArticle.get().getId()).isEqualTo(savedArticle.getId());
        assertThat(foundArticle.get().getTitle()).isEqualTo(savedArticle.getTitle());
    }

    @Test
    @DisplayName("문장에서 키워드 추출 기능 테스트")
    public void extractElasticSearch() throws IOException {

        Article article = Article.of("I like USA's tech");
        Article savedArticle = articleElasticRepository.save(article);

        AnalyzeRequest request = AnalyzeRequest.withGlobalAnalyzer("standard", savedArticle.getTitle());
        AnalyzeResponse response = client.indices().analyze(request, RequestOptions.DEFAULT);

        List<AnalyzeResponse.AnalyzeToken> tokens = response.getTokens();
        for (AnalyzeResponse.AnalyzeToken token : tokens) {
            String term = token.getTerm();
            System.out.println(term);
        }

    }

    @Test
    @DisplayName("단어 추출 테스트")
    public void extract() {

        Article article = new Article("1", "I like USA's tech");

        IndexQuery indexQuery = new IndexQueryBuilder()
                .withId(article.getId().toString())
                .withObject(article)
                .build();

        IndexCoordinates indexCoordinates = IndexCoordinates.of(Indices.ARTICLE_INDEX);
        String documentId = elasticsearchRestTemplate.index(indexQuery,indexCoordinates);

        String inputText = "I like USA's tech.";
        String searchTerm = "USA";
        int count = inputText.split(searchTerm, -1).length - 1;

        System.out.println("USA count : " + count);
    }

}
