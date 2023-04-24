package com.example.elasticsearch.elastic.service;

import com.example.elasticsearch.crawler.repository.StockJpaRepository;
import com.example.elasticsearch.elastic.repository.StockElasticRepository;
import com.example.elasticsearch.stock.domain.Article;
import com.example.elasticsearch.stock.domain.StockDbDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StockSearchService {

    @Autowired
    private final StockJpaRepository stockJpaRepository;

    @Autowired
    private final StockElasticRepository stockElasticRepository;

    public Article save(Article article) {
        return stockElasticRepository.save(article);
    }

    public Article findById(Long id) {
        stockJpaRepository.findById(id);
        return stockElasticRepository.findById(id).orElse(null);
    }

    public StockDbDto findByName(String stockName) {
        return stockJpaRepository.findByStockName(stockName);
    }
}
