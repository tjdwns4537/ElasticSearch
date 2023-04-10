package com.example.elasticsearch.stock.domain;

import com.example.elasticsearch.helper.StatusEnum;
import lombok.Data;

@Data
public class StockElasticDto {
    private StatusEnum statusEnum;
    private String message;
    private Object data;

    public StockElasticDto() {
        this.statusEnum = StatusEnum.BAD_REQUEST;
        this.message = null;
        this.data = null;
    }
}
