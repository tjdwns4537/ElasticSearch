package com.example.elasticsearch.redis.redisson;

import com.example.elasticsearch.elastic.service.ElasticCustomService;
import com.example.elasticsearch.helper.Indices;
import com.example.elasticsearch.thema.domain.Thema;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.Redisson;
import org.redisson.api.RBlockingDeque;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class RedissonService {

    @Autowired
    private final RedissonClient redissonClient;

    public boolean searchLock(String searchInfo) {
        RLock lock = redissonClient.getLock("searchLock:" + searchInfo);
        return lock.tryLock(); // Acquire the lock
    }

    public void searchUnlock(String searchInfo) {
        RLock lock = redissonClient.getLock("searchLock:" + searchInfo);
        if (lock.isHeldByCurrentThread()) {
            lock.unlock(); // Release the lock
        }
    }
}
