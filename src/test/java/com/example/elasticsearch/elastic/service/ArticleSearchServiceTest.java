package com.example.elasticsearch.elastic.service;

import co.elastic.clients.elasticsearch.core.IndexRequest;
import co.elastic.clients.elasticsearch.indices.CreateIndexRequest;
import com.example.elasticsearch.crawler.service.CrawlerService;
import com.example.elasticsearch.elastic.repository.ArticleElasticRepository;
import com.example.elasticsearch.helper.Indices;
import com.example.elasticsearch.stock.domain.Article;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.io.stream.StreamInput;
import org.elasticsearch.index.Index;
import org.elasticsearch.xcontent.XContentType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.IndexOperations;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.IndexQuery;
import org.springframework.data.elasticsearch.core.query.IndexQueryBuilder;

import java.io.IOException;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ArticleSearchServiceTest {

    @Autowired
    private ElasticsearchRestTemplate elasticsearchRestTemplate;

    @Autowired
    private  ElasticsearchOperations elasticsearchOperations;


    @Test
    @DisplayName("Elastic Search save test")
    public void save() throws JsonProcessingException {
        Article article = new Article(3L, "test");


        IndexQuery indexQuery = new IndexQueryBuilder()
                .withId(article.getId().toString())
                .withObject(article)
                .build();

        IndexCoordinates indexCoordinates = IndexCoordinates.of(Indices.ARTICLE_INDEX);

        String documentId = elasticsearchRestTemplate.index(indexQuery,indexCoordinates);
        assertThat(documentId).isNotNull();

        Article retrievedArticle = elasticsearchRestTemplate.get(article.getId().toString(), Article.class);
        assertThat(retrievedArticle).isNotNull();
        assertThat(retrievedArticle.getId()).isEqualTo(article.getId());
        assertThat(retrievedArticle.getTitle()).isEqualTo(article.getTitle());
    }

}
