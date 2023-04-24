package com.example.elasticsearch.redis.repository;

import com.example.elasticsearch.stock.domain.StockDbDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;

import java.util.Arrays;
import java.util.Set;

@SpringBootTest
class LikeArticleRepositoryTest {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Test
    public void getStockInfo() { //Redis orderSet 저장 < 종목이름, 가격, 등락율 >
        // 초기화
        ZSetOperations<String, String> ZSetOperations = redisTemplate.opsForZSet();

        StockDbDto stockDbDto = new StockDbDto();
        stockDbDto.setId(1L);
        stockDbDto.setStockName("aaa");
        stockDbDto.setStockPercent("3.0");
        stockDbDto.setStockPrice("10000");
        stockDbDto.setTradeCount("100000");

        StockDbDto stockDbDto2 = new StockDbDto();
        stockDbDto2.setId(2L);
        stockDbDto2.setStockName("abbb");
        stockDbDto2.setStockPercent("13.0");
        stockDbDto2.setStockPrice("1220000");
        stockDbDto2.setTradeCount("41100000");

        StockDbDto stockDbDto3 = new StockDbDto();
        stockDbDto3.setId(3L);
        stockDbDto3.setStockName("abcccc");
        stockDbDto3.setStockPercent("323.5");
        stockDbDto3.setStockPrice("1000000");
        stockDbDto3.setTradeCount("10230000");

        double percent = Double.parseDouble(stockDbDto.getStockPercent());
        double percent2 = Double.parseDouble(stockDbDto2.getStockPercent());
        double percent3 = Double.parseDouble(stockDbDto3.getStockPercent());

        // 저장
        ZSetOperations.add("stock", stockDbDto.getStockName(), percent);
        ZSetOperations.add("stock", stockDbDto2.getStockName(), percent2);
        ZSetOperations.add("stock", stockDbDto3.getStockName(), percent3);

        // 출력
        Set<String> range = ZSetOperations.range("stock", 0, 5);
        System.out.println("range = " + Arrays.toString(range.toArray()));
    }
}
