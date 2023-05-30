package com.example.elasticsearch.elastic.service;

import com.example.elasticsearch.article.domain.ArticleEls;
import com.example.elasticsearch.elastic.repository.ThemaElasticRepository;
import com.example.elasticsearch.thema.domain.Thema;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ThemaElasticService {
    @Autowired private final ThemaElasticRepository themaElasticRepository;

    public void themaSave(Thema thema) {
        Optional<Thema> themas = themaElasticRepository.findByThemaName(thema.getThemaName());
        if (!themas.isPresent()) {
            themaElasticRepository.save(thema);
        }
    }

    public Optional<Thema> findByKeyword(String keyword) {
        return themaElasticRepository.findByThemaName(keyword);
    }
}
