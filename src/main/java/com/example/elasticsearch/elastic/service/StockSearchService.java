package com.example.elasticsearch.elastic.service;

import com.example.elasticsearch.elastic.repository.StockElasticRepository;
import com.example.elasticsearch.stock.domain.Stock;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StockSearchService {

    @Autowired
    private final StockElasticRepository stockElasticRepository;

    public Stock save(Stock stock) {
        return stockElasticRepository.save(stock);
    }

    public Stock findById(Long id) {
        return stockElasticRepository.findById(id).orElse(null);
    }
}
