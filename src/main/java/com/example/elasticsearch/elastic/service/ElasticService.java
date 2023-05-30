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

    public ArticleEls save(ArticleEls articleEls) {
        ArticleEls save = articleElasticRepository.save(articleEls);
        return save;
    }

    public List<ArticleEls> ContainByKeyword(String keyword) {
        List<ArticleEls> byTitle = articleElasticRepository.findByTitleContaining(keyword);

        return byTitle;
    }

    public List<ArticleEls> findByTitle(String title) {
        return articleElasticRepository.findByTitle(title);
    }

    public void deleteAll() {
        articleElasticRepository.deleteAll();
    }

    public void deleteById(String uid) {
        articleElasticRepository.deleteById(uid);
    }
}
