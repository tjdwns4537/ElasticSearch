package com.example.elasticsearch.crawler.repository;

import com.example.elasticsearch.stock.domain.StockDbDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StockJpaRepository extends JpaRepository<StockDbDto, Long>, CustomStockRepository {
//    StockDbDto findByStockName(String stockName);
}
