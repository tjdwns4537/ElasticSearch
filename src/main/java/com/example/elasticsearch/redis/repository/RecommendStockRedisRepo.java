package com.example.elasticsearch.redis.repository;

import com.example.elasticsearch.stock.domain.FinanceStockRedis;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Repository
@Slf4j
public class RecommendStockRedisRepo {
    private final String STOCK = "RECOMMEND_STOCK";

    private RedisTemplate<String, FinanceStockRedis> redisObjectTemplate;

    private ZSetOperations<String, FinanceStockRedis> zSetOperations;


    @Autowired
    public RecommendStockRedisRepo(RedisTemplate<String, FinanceStockRedis> redisObjectTemplate) {
        this.redisObjectTemplate = redisObjectTemplate;
        this.zSetOperations = redisObjectTemplate.opsForZSet();
    }

    @PostConstruct
    public void init() {
        redisObjectTemplate.delete(STOCK);
    }

    public void saveStockRanking(FinanceStockRedis financeStockRedis) { //Redis orderSet 저장 < 종목이름, 가격, 등락율 >
        try{
            double percent = Double.parseDouble(financeStockRedis.getProfitPercent());
            zSetOperations.add(STOCK, financeStockRedis, percent);
            log.info("redis save : {}, {}", financeStockRedis, percent);
        }
        catch (NumberFormatException e){
            zSetOperations.add(STOCK, financeStockRedis, -99999.0);
            log.info("redis save : {}, {}", financeStockRedis.getStockName(), -99999.0);
        }
    }

    public ArrayList<FinanceStockRedis> getStockRanking() { // 출력
        Set<FinanceStockRedis> range = zSetOperations.reverseRange(STOCK, 0, -1);
        ArrayList<FinanceStockRedis> list = new ArrayList<>(range);
        if(list.size() > 3){
            return new ArrayList<>(list.subList(0, 3));
        }
        return list;
    }
}
