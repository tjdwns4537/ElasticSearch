package com.example.elasticsearch.crawler.repository;

import com.example.elasticsearch.stock.domain.Stock;
import com.example.elasticsearch.stock.domain.StockDbDto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StockJpaRepository extends JpaRepository<StockDbDto, Long> {
}
