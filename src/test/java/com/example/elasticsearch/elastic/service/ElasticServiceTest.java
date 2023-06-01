package com.example.elasticsearch.elastic.service;

import com.example.elasticsearch.article.domain.ArticleEls;
import com.example.elasticsearch.elastic.repository.ArticleElasticRepository;
import com.example.elasticsearch.helper.Indices;
import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.indices.AnalyzeRequest;
import org.elasticsearch.client.indices.AnalyzeResponse;
import org.elasticsearch.rest.RestStatus;
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


import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class ElasticServiceTest {

    @Autowired
    private ElasticsearchRestTemplate elasticsearchRestTemplate;

    @Autowired
    private ArticleElasticRepository articleElasticRepository;

    @Autowired
    private RestHighLevelClient client;

    @Autowired
    private ArticleElasticCustomService articleElasticCustomService;

    @Autowired
    private ElasticService elasticService;

    @Test
    @DisplayName("문장 유사성 테스트")
    public void similarTest() {

        /** TODO
         *  - 유사성 테스트 다시해볼것
         * **/

        elasticService.deleteAll();

        elasticService.articleSave(ArticleEls.of("오늘 반도체 시장은 망했다."));
        elasticService.articleSave(ArticleEls.of("내일 폐반도체 망했다."));
        elasticService.articleSave(ArticleEls.of("반도체가 망했다."));
        String sentence = "반도체";

        List<String> similarWords = articleElasticCustomService.findSimilarWords(Indices.ARTICLE_INDEX, sentence);

        for (String similarSentence : similarWords) {
            System.out.println(similarSentence);
        }
    }

    public boolean analyzeJavaWord(String word) throws IOException {
        BufferedReader reader = new BufferedReader(
                new FileReader("/Users/parksungjun/Desktop/ElasticSearchProject/ElasticSearch/src/main/resources/static/elastic/positive_words_self.txt")
        );
        String str;
        while ((str = reader.readLine()) != null) {
            if(word.equals(str)){
                System.out.println("단어 : " + word + "비교 : " + str);
                return true;
            }
        }

        reader.close();
        return false;
    }

    @Test
    @DisplayName("단어 개수 체크")
    public void deleteAndSave() throws IOException {
        String word = "기아자동차 15년도 실적 흑자 달성";
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

        AnalyzeRequest request = AnalyzeRequest.withIndexAnalyzer(Indices.ARTICLE_INDEX, "standard", word);
        AnalyzeResponse response = client.indices().analyze(request, RequestOptions.DEFAULT);

        Map<String, Integer> wordCountMap = new HashMap<>();
        int positiveCount = 0;
        int negativeCount = 0;

        for (AnalyzeResponse.AnalyzeToken token : response.getTokens()) {
            String term = token.getTerm();
            if (analyzeJavaWord(term)) {
                positiveCount++;
            } else if (term.equals("false")) {
                negativeCount++;
            }
            int count = wordCountMap.getOrDefault(term, 0);
            wordCountMap.put(term, count + 1);
        }

        System.out.println("Positive count: " + positiveCount);
        System.out.println("Negative count: " + negativeCount);
        System.out.println("Word count map: " + wordCountMap);

        client.close();
    }

    @Test
    @DisplayName("Elastic Search Teample save TEST")
    public void saveTemplate() {
        ArticleEls articleEls = new ArticleEls("1", "I like USA's tech");

        IndexQuery indexQuery = new IndexQueryBuilder()
                .withId(articleEls.getId())
                .withObject(articleEls)
                .build();

        IndexCoordinates indexCoordinates = IndexCoordinates.of(Indices.ARTICLE_INDEX);

        String documentId = elasticsearchRestTemplate.index(indexQuery,indexCoordinates);
        assertThat(documentId).isNotNull();

        ArticleEls retrievedArticleEls = elasticsearchRestTemplate.get(articleEls.getId(), ArticleEls.class);
        assertThat(retrievedArticleEls).isNotNull();
        String id = retrievedArticleEls.getId();
        String title = retrievedArticleEls.getTitle();
        assertThat(id).isEqualTo(articleEls.getId());
        assertThat(title).isEqualTo(articleEls.getTitle());

        System.out.println("save id : "+id);
        System.out.println("save title : "+title);
    }

    @Test
    @DisplayName("ElasticSearch Repository Test")
    public void saveRepository() {
        ArticleEls articleEls = ArticleEls.of("I like USA's tech");
        ArticleEls savedArticleEls = articleElasticRepository.save(articleEls);

        Optional<ArticleEls> foundArticle = articleElasticRepository.findById(savedArticleEls.getId());

        assertThat(foundArticle).isPresent();
        assertThat(foundArticle.get().getId()).isEqualTo(savedArticleEls.getId());
        assertThat(foundArticle.get().getTitle()).isEqualTo(savedArticleEls.getTitle());
    }

    @Test
    @DisplayName("문장에서 키워드 추출 기능 테스트")
    public void extractElasticSearch() throws IOException {
        ArticleEls articleEls = ArticleEls.of("경찰과 도둑, 개정법, 축구와 야구, 전세 사기");
        ArticleEls savedArticleEls = articleElasticRepository.save(articleEls);

        AnalyzeRequest request = AnalyzeRequest.withGlobalAnalyzer("nori", savedArticleEls.getTitle());
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
        ArticleEls articleEls = new ArticleEls("1", "I like USA's tech");

        IndexQuery indexQuery = new IndexQueryBuilder()
                .withId(articleEls.getId().toString())
                .withObject(articleEls)
                .build();

        IndexCoordinates indexCoordinates = IndexCoordinates.of(Indices.ARTICLE_INDEX);
        String documentId = elasticsearchRestTemplate.index(indexQuery,indexCoordinates);

        String inputText = "I like USA's tech.";
        String searchTerm = "USA";
        int count = inputText.split(searchTerm, -1).length - 1;

        System.out.println("USA count : " + count);
    }

}
