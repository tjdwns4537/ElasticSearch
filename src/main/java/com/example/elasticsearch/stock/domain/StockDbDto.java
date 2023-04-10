package com.example.elasticsearch.stock.domain;

import lombok.Data;
import lombok.Getter;
import org.springframework.data.annotation.Id;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Table;

@Getter
@Table(name = "stock")
public class StockDbDto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "stock_name")
    private String name;

    @Column(name = "stock_price")
    private String price;

    @Column(name = "stock_percent")
    private String percent;

    @Column(name = "trade_count")
    private String tradeCount;

    public StockDbDto(String name, String price, String percent, String tradeCount) {
        this.name = name;
        this.price = price;
        this.percent = percent;
        this.tradeCount = tradeCount;
    }

    public static StockDbDto from(String name, String price, String percent, String tradeCount) {
        return new StockDbDto(name, price, percent, tradeCount);
    }
}
