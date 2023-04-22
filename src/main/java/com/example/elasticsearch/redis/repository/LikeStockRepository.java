package com.example.elasticsearch.redis.repository;

import com.example.elasticsearch.stock.domain.StockDbDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class LikeStockRepository {

    private final String STOCK = "LIKESTOCK";

    private RedisTemplate<String, String> redisTemplate;
    private ZSetOperations<String, String> ZSetOperations;

    @Autowired
    public LikeStockRepository(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
        this.ZSetOperations = redisTemplate.opsForZSet();

    }

    @PostConstruct
    public void init() {
        redisTemplate.delete(STOCK);
    }

    public void setStockRanking(StockDbDto stockDbDto) { //Redis orderSet 저장 < 종목이름, 가격, 등락율 >
        double percent = Double.parseDouble(stockDbDto.getStockPercent());
        ZSetOperations.add(STOCK, "["+stockDbDto.getStockName() + "] 현재가: "
                        +stockDbDto.getStockPrice()+"원 - 등락율 : "
                        +stockDbDto.getStockPercent() + "%"
                , percent);
    }

    public List<String> getStockRanking() { // 출력
        Set<String> range = ZSetOperations.reverseRange(STOCK, 0, -1);
        List<String> list = new ArrayList<>(range);
        if(list.size() > 5){
            return list.subList(0, 5);
        }
        return list;
    }
}
