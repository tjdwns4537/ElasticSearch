package com.example.elasticsearch.stock.service;

import com.example.elasticsearch.stock.repository.StockElasticRepository;
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

    public Stock findById(String id) {
        return stockElasticRepository.findById(id).orElse(null);
    }
}
