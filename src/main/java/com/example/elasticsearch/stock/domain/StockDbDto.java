package com.example.elasticsearch.stock.domain;

import lombok.Data;
import org.springframework.data.annotation.Id;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Table;

@Data
@Table(name = "stock")
public class StockDbDto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "stock_name")
    private String name;

    @Column(name = "stock_price")
    private Integer price;

    @Column(name = "stock_percent")
    private Double percent;

    @Column(name = "trade_count")
    private Long tradeCount;
}
