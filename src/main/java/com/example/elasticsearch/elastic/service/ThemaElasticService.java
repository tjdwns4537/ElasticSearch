package com.example.elasticsearch.elastic.service;

import com.example.elasticsearch.elastic.repository.ThemaElasticRepository;
import com.example.elasticsearch.helper.Indices;
import com.example.elasticsearch.thema.domain.Thema;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ThemaElasticService {
    @Autowired private final ThemaElasticRepository themaElasticRepository;

    public void clear() {
        log.info("테마 데이터 비우기");
        themaElasticRepository.deleteAll();
    }

    public void themaSave(Thema thema) {
        Optional<Thema> existThema = themaElasticRepository.findByThemaName(thema.getThemaName());
        if (existThema.isPresent()) {
            return;
        }
        log.info("테마 데이터 저장: {}", thema.getThemaName());
        themaElasticRepository.save(thema);
    }

    public Optional<Thema> findByKeyword(String keyword) {
        return themaElasticRepository.findByThemaName(keyword);
    }
}
