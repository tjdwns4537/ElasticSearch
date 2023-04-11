package com.example.elasticsearch;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@SpringBootApplication
public class ElasticSearchApplication {

    public static void main(String[] args) {
        SpringApplication.run(ElasticSearchApplication.class, args);
    }

}
