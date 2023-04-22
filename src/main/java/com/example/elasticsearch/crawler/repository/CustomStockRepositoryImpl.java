package com.example.elasticsearch.crawler.repository;

import com.example.elasticsearch.stock.domain.QStockDbDto;
import com.example.elasticsearch.stock.domain.QStockLikeDto;
import com.example.elasticsearch.stock.domain.StockDbDto;
import com.example.elasticsearch.stock.domain.StockLikeDto;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Repository
@Transactional
@Slf4j
public class CustomStockRepositoryImpl implements CustomStockRepository {

    @Autowired
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public StockDbDto findByStockName(String stockName) {
        QStockDbDto qStockDbDto = QStockDbDto.stockDbDto;

        StockDbDto resultStock = jpaQueryFactory.select(qStockDbDto)
                .from(qStockDbDto)
                .where(qStockDbDto.stockName.eq(stockName))
                .fetchOne();

        return resultStock;
    }

    @Override
    public StockLikeDto findByLikeStockName(String stockName) {
        QStockLikeDto qStockLikeDto = QStockLikeDto.stockLikeDto;

        return jpaQueryFactory.select(qStockLikeDto)
                .from(qStockLikeDto)
                .where(qStockLikeDto.likeStock.eq(stockName))
                .fetchOne();
    }
}
