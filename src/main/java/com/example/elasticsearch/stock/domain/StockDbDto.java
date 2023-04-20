package com.example.elasticsearch.stock.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity(name = "stock")
@Table(name = "stock")
public class StockDbDto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "stock_id")
    private Long id;

    @Column(name = "stock_name")
    private String stockName;

    @Column(name = "stock_price")
    private String stockPrice;

    @Column(name = "stock_percent")
    private String stockPercent;

    @Column(name = "trade_count")
    private String tradeCount;

    public StockDbDto() {
        this.stockName = null;
        this.stockPrice = null;
        this.stockPercent = null;
        this.tradeCount = null;
    }

    public StockDbDto(String stockName, String stockPrice, String stockPercent, String tradeCount) {
        this.stockName = stockName;
        this.stockPrice = stockPrice;
        this.stockPercent = stockPercent;
        this.tradeCount = tradeCount;
    }

    public static StockDbDto of(String name, String price, String percent, String tradeCount) {
        return new StockDbDto(name, price, percent, tradeCount);
    }
}
