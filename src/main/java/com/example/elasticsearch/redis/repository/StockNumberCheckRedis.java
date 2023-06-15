package com.example.elasticsearch.redis.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class StockNumberCheckRedis {

    private final String STOCK_LIST = "RECOMMEND_STOCK_LIST";
    private RedisTemplate<String, String> redisTemplate;
    private ListOperations<String, String> listOperations;


    @Autowired
    public StockNumberCheckRedis(
            RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
        this.listOperations = redisTemplate.opsForList();
    }

    public Boolean checkStockNumber(String stockNumber) {
        if(listOperations.range(STOCK_LIST, 0, -1).contains(stockNumber)) return false;
        listOperations.rightPush(STOCK_LIST, stockNumber);
        return true;
    }
}
