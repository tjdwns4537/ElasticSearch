package com.example.elasticsearch.elastic.repository;

import com.example.elasticsearch.article.domain.ArticleEls;
import com.example.elasticsearch.thema.domain.Thema;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ThemaElasticRepository extends ElasticsearchRepository<Thema, String> {
    Optional<Thema> findByThemaName(String keyword);
}
