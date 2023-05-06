package com.example.elasticsearch.elastic.service;

import com.example.elasticsearch.article.domain.Article;
import com.example.elasticsearch.crawler.service.CrawlerService;
import com.example.elasticsearch.elastic.repository.ArticleElasticRepository;
import com.example.elasticsearch.helper.Indices;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.indices.AnalyzeRequest;
import org.elasticsearch.client.indices.AnalyzeResponse;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.IndexQuery;
import org.springframework.data.elasticsearch.core.query.IndexQueryBuilder;

import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.common.settings.Settings;

import org.elasticsearch.action.search.SearchRequest;


import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class ArticleSearchServiceTest {

    @Autowired
    private CrawlerService crawlerService;

    @Autowired
    private ElasticsearchRestTemplate elasticsearchRestTemplate;

    @Autowired
    private ArticleElasticRepository articleElasticRepository;

    @Autowired
    private RestHighLevelClient client;

    @Test
    @DisplayName("텍스트 감정 분석 테스트")
    public void analysisText() throws IOException {
        // 이름이 "my_index"인 Elastic 검색 인덱스를 만드는 새 요청을 만듭니다.
        CreateIndexRequest request = new CreateIndexRequest("my_index");

        // "my_index"에 대한 인덱스 설정을 설정합니다. 이 예제에서는 샤드 수를 1로 설정하고 복제본 수를 0으로 설정합니다.
        request.settings(Settings.builder()
                .put("index.number_of_shards", 1)
                .put("index.number_of_replicas", 0)
        );

        // "my_index"에 대한 인덱스 매핑을 설정합니다. 이 예에서는 "sentment" 분석기가 구성된 "my_field" 필드에 대한 매핑을 정의합니다.
//        XContentBuilder builder = XContentFactory.jsonBuilder();
//        builder.startObject();
//        {
//            builder.startObject("my_type");
//            {
//                builder.startObject("properties");
//                {
//                    builder.startObject("my_field");
//                    {
//                        builder.field("type", "text");
//                        builder.field("analyzer", "sentiment");
//                    }
//                    builder.endObject();
//                }
//                builder.endObject();
//            }
//            builder.endObject();
//        }
//        builder.endObject();
//
//        request.mapping(builder);
//
//        // 인덱스 만들기 요청을 Elastic search로 보내고 응답을 반환합니다. 클라이언트 개체는 Elasticsearch Java High-Level REST Client의 인스턴스입니다.
//        CreateIndexResponse response = client.indices().create(request, RequestOptions.DEFAULT);


//        SearchRequest request2 = new SearchRequest("my_index");
//        SearchSourceBuilder builder2 = new SearchSourceBuilder();
//        builder2.query(QueryBuilders.matchQuery("my_field", "positive"));
//        request.source(builder);
//
//        SearchResponse response2 = client.search(request2, RequestOptions.DEFAULT);
//
//        SearchHits hits = response2.getHits();
//        for (SearchHit hit : hits) {
//            String myField = hit.getSourceAsMap().get("my_field").toString();
//            System.out.println("my_field value: " + myField);
//        }
    }

    @Test
    @DisplayName("Elastic Search Teample save TEST")
    public void saveTemplate() {
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
        Article article = Article.of("경찰과 도둑, 개정법, 축구와 야구, 전세 사기");
        Article savedArticle = articleElasticRepository.save(article);

        AnalyzeRequest request = AnalyzeRequest.withGlobalAnalyzer("nori", savedArticle.getTitle());
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
