package com.example.elasticsearch.service;

import com.example.elasticsearch.document.ArticleDoc;
import com.example.elasticsearch.repository.ArticleRepository;
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
