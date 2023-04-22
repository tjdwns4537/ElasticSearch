package com.example.elasticsearch.crawler.repository;

import com.example.elasticsearch.stock.domain.QStockDbDto;
import com.example.elasticsearch.stock.domain.StockDbDto;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class CustomStockRepositoryImplTest {

    @Autowired
    private EntityManager em;


    @Test
    @DisplayName("주식 종목명으로 조회 테스트")
    public void findByName() {
        String sel = "기아";
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        QStockDbDto qStockDbDto = QStockDbDto.stockDbDto;

        StockDbDto stockDbDto = queryFactory.select(qStockDbDto)
                .from(qStockDbDto)
                .where(qStockDbDto.stockName.eq(sel))
                .fetchOne();

        System.out.println("kia : " + stockDbDto.getStockName() + " : " + stockDbDto.getStockPrice());;
    }

}
