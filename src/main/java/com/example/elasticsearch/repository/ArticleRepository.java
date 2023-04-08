package com.example.elasticsearch.repository;

import com.example.elasticsearch.document.ArticleDoc;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArticleRepository extends ElasticsearchRepository<ArticleDoc, String> {
}
