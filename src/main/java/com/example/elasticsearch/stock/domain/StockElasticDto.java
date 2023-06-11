package com.example.elasticsearch.stock.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
public class StockElasticDto implements Serializable {

    /** 키워드 제목 */
    private String stockName;

    private String price;

    private String prevPriceCompare;

    private String prevPriceComparePercent;

    public StockElasticDto(String stockName, String price, String prevPriceCompare, String prevPriceComparePercent) {
        this.stockName = stockName;
        this.price = price;
        this.prevPriceCompare = prevPriceCompare;
        this.prevPriceComparePercent = prevPriceComparePercent;
    }

    public static StockElasticDto of(String stockName, String price, String prevPriceCompare, String prevPriceComparePercent) {
        return new StockElasticDto(stockName, price, prevPriceCompare, prevPriceComparePercent);
    }
}
