package com.example.elasticsearch.article.service;

import com.example.elasticsearch.article.domain.Article;
import com.example.elasticsearch.article.repository.ArticleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ArticleService {

    private final ArticleRepository articleRepository;

    public void save(Article article) {
        articleRepository.save(article);
    }

}
