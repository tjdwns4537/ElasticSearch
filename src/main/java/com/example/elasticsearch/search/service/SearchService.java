package com.example.elasticsearch.search.service;

import com.example.elasticsearch.crawler.service.CrawlerService;
import com.example.elasticsearch.kafka.service.CrawlingKafkaService;
import com.example.elasticsearch.redis.redisson.RedissonService;
import com.example.elasticsearch.redis.repository.RecommendStockRedisRepo;
import com.example.elasticsearch.search.domain.Search;
import com.example.elasticsearch.search.repository.SearchRepository;
import com.example.elasticsearch.stock.domain.FinanceStockRedis;
import com.example.elasticsearch.stock.domain.RelativeStockKafkaDto;
import com.example.elasticsearch.stock.domain.StockElasticDto;
import com.example.elasticsearch.stock.service.StockService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Slf4j
public class SearchService {

    @Autowired
    private final SearchRepository searchRepository;

    @Autowired
    private final RecommendStockRedisRepo recommendStockRedisRepo;

    @Autowired
    private final StockService stockService;

    @Autowired
    private final CrawlerService crawlerService;

    @Autowired
    private final RedissonService redissonService;

    @Autowired
    private final CrawlingKafkaService crawlingKafkaService;
    public void save(Search search) {
        searchRepository.save(search);
    }

    public static boolean isNumeric(String strNum) {
        if (strNum == null) {
            return false;
        }
        try {
            double d = Double.parseDouble(strNum);
        } catch (NumberFormatException nfe) {
            log.info("false : {}", strNum);
            return false;
        }
        return true;
    }

    public void saveBestStock(List<StockElasticDto> stocks) {
        for (int i=0; i<stocks.size(); i++) {
            String stockNameArg = stocks.get(i).getStockName();
            List<String> stockNumber = stockService.findStockNumber(stockNameArg);

            log.info("best stock stockNumber : {}", stockNumber);
            for (String j: stockNumber) {
                crawlingKafkaService.sendSTMessage(j, i%3);

                log.info("주식 넘버 조회 : {}", j);
                if(isNumeric(j)){
                    try {
                        String stockNumberArg = j;
                        int partition = i % 9;
                        crawlingKafkaService.sendTAMessage(stockNumberArg, partition);
                        log.info("조회 성공 : {}", stockNumberArg);
                    } catch (IndexOutOfBoundsException e) {
                        log.error("Index Error - findStockNumber 과정에서 주식 이름을 찾지 못해 에러가 발생했을 확률 높음");
                    }
                }
            }
        }
    }

//    public void anayzeBestStock(String stockName) {
//        StringBuilder strN = new StringBuilder();
//        StringBuilder i = new StringBuilder();
//        strN.append(stockName.substring(0, stockName.length() - 1));
//        i.append(stockName.substring(stockName.length() - 1, stockName.length()));
//        log.info("주식 넘버 조회 : {}", stockName);
//        if(isNumeric(stockName)){
//            try {
//                int partition = Integer.parseInt(i.toString()) % 9;
//                crawlingKafkaService.sendTAMessage(stockName, partition);
//                log.info("조회 성공 : {}", stockName);
//            } catch (IndexOutOfBoundsException e) {
//                log.error("Index Error - findStockNumber 과정에서 주식 이름을 찾지 못해 에러가 발생했을 확률 높음");
//            }
//        }
//    }

    public ArrayList<FinanceStockRedis> extractBestStock() {
        boolean lockAcquired = false;
        ArrayList<FinanceStockRedis> stockRanking = recommendStockRedisRepo.getStockRanking();
//        for (FinanceStockRedis i : stockRanking) {
//            log.info("stock name : {}",i.getStockName());
//        }
//        try {
//            if (stockRanking.isEmpty()) {
//                log.info("lock 획득");
//                lockAcquired = redissonService.fiLock("fi", 15, TimeUnit.SECONDS); // 락 획득 시도에 timeout 적용
//                if (!lockAcquired) {
//                    log.info("Failed to acquire lock for searchInfo: {}", "fi");
//                    // lock을 획득하지 못한 경우에 대한 처리를 수행 (예: 재시도, 예외 처리 등)
//                }
//            }
//        } finally {
//            if (lockAcquired) {
//                redissonService.Unlock("fi");
//            }
//        }
//
//        for (FinanceStockRedis i : stockRanking) {
//            log.info("lock 해체 : {}", i.getStockName());
//        }
        return stockRanking;
    }

}
