package com.example.elasticsearch.redis.redisson;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@Slf4j
@RequiredArgsConstructor
public class RedissonService {

    @Autowired
    private final RedissonClient redissonClient;

    public boolean fiLock(String key, long timeout, TimeUnit unit) {
        RLock lock = redissonClient.getLock("key:" + key);
        try {
            return lock.tryLock(timeout, unit); // Acquire the lock with timeout
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt(); // Preserve interrupt status
            return false;
        }
    }

    public boolean searchLock(String searchInfo) {
        RLock lock = redissonClient.getLock("key:" + searchInfo);
        return lock.tryLock(); // Acquire the lock
    }

    public void Unlock(String key) {
        RLock lock = redissonClient.getLock("key:" + key);
        if (lock.isHeldByCurrentThread()) {
            lock.unlock(); // Release the lock
        }
    }
}
