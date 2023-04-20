package com.example.elasticsearch.crawler.repository;

import com.example.elasticsearch.stock.domain.StockLikeDto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StockListJpaRepository extends JpaRepository<StockLikeDto, Long> {
}
