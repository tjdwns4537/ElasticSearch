package com.example.elasticsearch.elastic.repository;

import com.example.elasticsearch.article.domain.Article;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArticleElasticRepository extends ElasticsearchRepository<Article, String> {
    List<Article> findByTitleContaining(String keyword);

    @Query("{\"match\": {\"title\": {\"query\": \"?0\"}}}")
    List<Article> findByTitle(String title);
}
