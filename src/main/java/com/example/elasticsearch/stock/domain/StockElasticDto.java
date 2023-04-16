package com.example.elasticsearch.stock.domain;

import com.example.elasticsearch.helper.StatusEnum;
import lombok.Data;

@Data
public class StockElasticDto {
    private StatusEnum statusEnum;
    private Object data;
    private String message;

    public StockElasticDto() {
        this.statusEnum = StatusEnum.BAD_REQUEST;
        this.data = null;
        this.message = null;
    }
}
