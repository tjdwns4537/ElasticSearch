package com.example.elasticsearch.elastic.repository;

import com.example.elasticsearch.stock.domain.Article;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StockElasticRepository extends ElasticsearchRepository<Article, Long> {
}
