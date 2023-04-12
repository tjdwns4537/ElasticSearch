package com.example.elasticsearch.crawler.repository;

import com.example.elasticsearch.stock.domain.QStockDbDto;
import com.example.elasticsearch.stock.domain.Stock;
import com.example.elasticsearch.stock.domain.StockDbDto;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@Transactional
public class CustomStocRepositoryImpl implements CustomStockRepository {

    @Autowired
    EntityManager em;

    @Override
    public StockDbDto findByStockName(String stockName) {
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        QStockDbDto qStockDbDto = QStockDbDto.stockDbDto;

        StockDbDto stockDbDto = queryFactory.select(qStockDbDto)
                .from(qStockDbDto)
                .where(qStockDbDto.stockName.eq(stockName))
                .fetchOne();    }
}
