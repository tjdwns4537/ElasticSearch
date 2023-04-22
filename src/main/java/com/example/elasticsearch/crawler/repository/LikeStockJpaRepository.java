package com.example.elasticsearch.crawler.repository;

import com.example.elasticsearch.stock.domain.StockLikeDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LikeStockJpaRepository extends JpaRepository<StockLikeDto, Long>, CustomStockRepository{
}
