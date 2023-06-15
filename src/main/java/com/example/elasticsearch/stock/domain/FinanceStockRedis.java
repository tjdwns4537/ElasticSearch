package com.example.elasticsearch.stock.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FinanceStockRedis implements Serializable {
    private String stockName;
    private String stockNumber;
    private String sales;
    private String salesPercent;
    private String profit;
    private String profitPercent;
    private String currentProfit;
    private String currentProfitPercent;
    private String potential;

    public FinanceStockRedis(String stockNumber) {
        this.stockNumber = stockNumber;
    }

    public static FinanceStockRedis of(String stockName, String stockNumber, String sales, String salesPercent, String profit, String profitPercent, String currentProfit, String currentProfitPercent, String potential) {
        return new FinanceStockRedis(stockName, stockNumber, sales, salesPercent, profit, profitPercent, currentProfit, currentProfitPercent, potential);
    }
}
