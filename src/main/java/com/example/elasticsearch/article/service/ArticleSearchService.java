package com.example.elasticsearch.article.service;

import com.example.elasticsearch.article.repository.ArticleRepository;
import com.example.elasticsearch.elastic.document.ArticleDoc;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ArticleSearchService {

    @Autowired
    private final ArticleRepository articleRepository;

    public ArticleDoc save(ArticleDoc articleDoc) {
        return articleRepository.save(articleDoc);
    }

    public ArticleDoc findById(String id) {
        return articleRepository.findById(id).orElse(null);
    }
}
