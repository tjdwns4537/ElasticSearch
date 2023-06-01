package com.example.elasticsearch.elastic.service;

import com.example.elasticsearch.article.domain.ArticleEls;
import com.example.elasticsearch.elastic.repository.ThemaElasticRepository;
import com.example.elasticsearch.helper.Indices;
import com.example.elasticsearch.thema.domain.Thema;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.xcontent.XContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ThemaElasticService {
    @Autowired private final ThemaElasticRepository themaElasticRepository;
    @Autowired private final RestHighLevelClient client;

    public Optional<Thema> findByThemaName(Thema thema) {
        return themaElasticRepository.findByThemaName(thema.getThemaName());
    }

    public void themaSave(Thema thema) {
        Optional<Thema> existThema = themaElasticRepository.findByThemaName(thema.getThemaName());
        if (!existThema.isPresent()) {
            themaElasticRepository.save(thema);
            return;
        }

        if ((thema.getFirstStock() != null) && !thema.getFirstStock().isEmpty()) {
            try {
                UpdateRequest updateRequest = new UpdateRequest(Indices.ARTICLE_THEMA_INDEX,
                        existThema.get().getId());

                updateRequest.doc("firstStock", thema.getFirstStock());
                updateRequest.doc("secondStock", thema.getSecondStock());

                client.update(updateRequest, RequestOptions.DEFAULT);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public Optional<Thema> findByKeyword(String keyword) {
        return themaElasticRepository.findByThemaName(keyword);
    }
}
