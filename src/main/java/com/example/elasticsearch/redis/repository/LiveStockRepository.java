package com.example.elasticsearch.redis.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;

@Service
public class LiveStockRepository {

    private final String STOCK = "LIVESTOCK";

    private RedisTemplate<String, String> redisTemplate;
    private ListOperations<String, String> listOperations;

    @Autowired
    public LiveStockRepository(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
        this.listOperations = redisTemplate.opsForList();
    }

    @PostConstruct
    public void init() {
        redisTemplate.delete(STOCK);
    }

    public void setStockLive(String liveStock) { //Redis orderSet 저장 < 종목이름, 가격, 등락율 >
        listOperations.rightPush(STOCK, liveStock);
    }

    public List<String> getStockLive() { // 출력
        return listOperations.range(STOCK, 0, -1);
    }
}
