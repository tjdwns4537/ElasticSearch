package com.example.elasticsearch.crawler.repository;

import com.example.elasticsearch.stock.domain.StockDbDto;

import java.util.List;

public interface CustomStockRepository {
    StockDbDto findByStockName(String stockName);
}
