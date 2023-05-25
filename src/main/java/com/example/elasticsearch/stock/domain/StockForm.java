package com.example.elasticsearch.stock.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StockForm {
    private String stockName;

    public boolean isNull() {
        if(stockName.isEmpty()) return true;
        else return false;
    }
}
