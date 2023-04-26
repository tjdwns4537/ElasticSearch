package com.example.elasticsearch.elastic.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ArticleSearchQueryRepository {

    @Autowired
    private final ElasticsearchOperations elasticsearchOperations;


}
