package com.example.elasticsearch.elastic.repository;

import com.example.elasticsearch.article.domain.ArticleEls;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArticleElasticRepository extends ElasticsearchRepository<ArticleEls, String> {
    List<ArticleEls> findByTitleContaining(String keyword);

    List<ArticleEls> findByTitle(String title);

    @Override
    void deleteAll();
}
