package com.example.elasticsearch.redis.repository;

import com.example.elasticsearch.stock.domain.FinanceStockRedis;
import com.example.elasticsearch.thema.domain.Thema;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Set;

@Repository
@Slf4j
public class ThemaRedisRepo {
    private final String STOCK = "THEMA_STOCK";

    private RedisTemplate<String, Thema> redisThemaTemplate;

    private ZSetOperations<String, Thema> zSetOperations;


    @Autowired
    public ThemaRedisRepo(RedisTemplate<String, Thema> redisThemaTemplate) {
        this.redisThemaTemplate = redisThemaTemplate;
        this.zSetOperations = redisThemaTemplate.opsForZSet();
    }

    public void deleteThemaRedis() {
        redisThemaTemplate.delete(STOCK);
    }

    public void saveThema(Thema thema) { //Redis orderSet 저장 < thema name, 등락율 >
        String extractPercent = thema.getPercent().substring(1, thema.getPercent().length() - 1);
        double percent = Double.parseDouble(extractPercent);
        zSetOperations.add(STOCK, thema, percent);
        log.info("redis save : {}, {}", thema.getThemaName(), percent);
    }

    public ArrayList<Thema> getThemaRanking() { // 출력
        Set<Thema> range = zSetOperations.reverseRange(STOCK, 0, -1);
        ArrayList<Thema> list = new ArrayList<>(range);
        if(list.size() > 20){
            return new ArrayList<>(list.subList(0, 20));
        }
        return list;
    }
}
