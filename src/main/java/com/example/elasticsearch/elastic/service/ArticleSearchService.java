package com.example.elasticsearch.elastic.service;

import com.example.elasticsearch.elastic.repository.ArticleElasticRepository;
import com.example.elasticsearch.article.domain.Article;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.query.IndexQuery;
import org.springframework.data.elasticsearch.core.query.IndexQueryBuilder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ArticleSearchService {

    @Autowired
    private final ArticleElasticRepository articleElasticRepository;

    public Article save(Article article) {
        return articleElasticRepository.save(article);
    }

    public List<Article> findByKeyword(String keyword) {
        return articleElasticRepository.findByTitleContaining(keyword);
    }
}
