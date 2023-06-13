package com.example.elasticsearch.redis.repository;

import com.example.elasticsearch.stock.domain.FinanceStockRedis;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Repository;

@Repository
@Slf4j
public class RedissonSearchRepo {
    private final String STOCK = "RECOMMEND_STOCK";

    private RedisTemplate<String, String> redisTemplate;

    private ValueOperations<String, String> valueOperations;


    @Autowired
    public RedissonSearchRepo(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
        this.valueOperations = redisTemplate.opsForValue();
    }

    public void stopValue(FinanceStockRedis financeStockList) {
//        redisTemplate.opsForValue().set("financeStockList", financeStockList);
    }
}
