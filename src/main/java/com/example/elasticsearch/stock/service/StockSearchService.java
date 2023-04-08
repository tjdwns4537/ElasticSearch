package com.example.elasticsearch.stock.service;

import com.example.elasticsearch.stock.repository.StockRepository;
import com.example.elasticsearch.stock.domain.Stock;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StockSearchService {

    @Autowired
    private final StockRepository stockRepository;

    public Stock save(Stock stock) {
        return stockRepository.save(stock);
    }

    public Stock findById(String id) {
        return stockRepository.findById(id).orElse(null);
    }
}
