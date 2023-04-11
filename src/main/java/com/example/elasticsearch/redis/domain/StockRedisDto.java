package com.example.elasticsearch.redis.domain;

import com.example.elasticsearch.stock.domain.StockDbDto;
import lombok.Getter;
import org.springframework.data.redis.core.RedisHash;

import java.util.ArrayList;
import java.util.List;

@Getter
@RedisHash
public class StockRedisDto {
    List<StockDbDto> stockDbDtoList = new ArrayList<>();

    public StockRedisDto(StockDbDto stockDbDto) {
        stockDbDtoList.add(stockDbDto);
    }

    public static StockRedisDto from(StockDbDto stockDbDto) {
        return new StockRedisDto(stockDbDto);
    }
}
