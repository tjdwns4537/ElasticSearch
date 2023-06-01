package com.example.elasticsearch.elastic.repository;

import com.example.elasticsearch.thema.domain.Thema;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ThemaElasticRepository extends ElasticsearchRepository<Thema, String> {
    Optional<Thema> findByThemaName(String keyword);
}
