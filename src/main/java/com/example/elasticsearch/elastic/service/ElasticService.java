package com.example.elasticsearch.elastic.service;

import com.example.elasticsearch.article.domain.ArticleEls;
import com.example.elasticsearch.elastic.repository.ArticleElasticRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ElasticService {

    @Autowired
    private final ArticleElasticRepository articleElasticRepository;

    public void articleSave(ArticleEls articleEls) {
        List<ArticleEls> existingArticles = articleElasticRepository.findByTitle(articleEls.getTitle());
        if (existingArticles.isEmpty()) {
            articleElasticRepository.save(articleEls);
        }
    }

    public void deleteAll() {
        articleElasticRepository.deleteAll();
    }

}
