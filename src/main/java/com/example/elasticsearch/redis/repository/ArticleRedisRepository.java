package com.example.elasticsearch.redis.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class ArticleRedisRepository {

    private final String ARTICLE = "ARTICLE";

    private RedisTemplate<String, String> redisTemplate;

    private ListOperations<String, String> listOperations;

    @Autowired
    public ArticleRedisRepository(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
        this.listOperations = redisTemplate.opsForList();;
    }

    public void save(String text) {
        listOperations.rightPush(ARTICLE, text);
    }

    public void findAll() {
        listOperations.rightPop(ARTICLE, listOperations.size(ARTICLE));
    }
}
