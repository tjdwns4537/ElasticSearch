package com.example.elasticsearch.repository;

import com.example.elasticsearch.document.ArticleDoc;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface ArticleRepositoryInterface extends ElasticsearchRepository<ArticleDoc, String> {
}
