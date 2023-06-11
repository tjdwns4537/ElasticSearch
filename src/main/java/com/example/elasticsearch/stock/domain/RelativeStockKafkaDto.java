package com.example.elasticsearch.stock.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;

@Getter
@Slf4j
@AllArgsConstructor
@NoArgsConstructor
@Setter
public class RelativeStockKafkaDto implements Serializable {
    private String stockName;
    private String stockNumber;

    public static RelativeStockKafkaDto of(String stockName, String stockNumber) {
        return new RelativeStockKafkaDto(stockName, stockNumber);
    }
}
