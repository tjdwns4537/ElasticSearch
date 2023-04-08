package com.example.elasticsearch.stock.domain;

import com.example.elasticsearch.helper.StatusEnum;
import lombok.Data;

@Data
public class StockDto {
    private StatusEnum statusEnum;
    private String message;
    private Object data;

    public StockDto() {
        this.statusEnum = StatusEnum.BAD_REQUEST;
        this.message = null;
        this.data = null;
    }
}
