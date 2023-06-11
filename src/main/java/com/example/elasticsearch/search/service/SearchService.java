package com.example.elasticsearch.search.service;

import com.example.elasticsearch.crawler.service.CrawlerService;
import com.example.elasticsearch.kafka.service.CrawlingKafkaService;
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

    public void save(Search search) {
        searchRepository.save(search);
    }

    public Optional<Search> findById(Long id) {
        return searchRepository.findById(id);
    }

    public List<Search> findByAll() {
       return searchRepository.findAll();
    }

    public void saveBestStock(String stock) {
        List<String> stockNumber = stockService.findStockNumber(stock);
        log.info("best stock stockNumber : {}", stockNumber);
        try {
            String stockNameArg = stock;
            String stockNumberArg = stockNumber.get(0);
            RelativeStockKafkaDto relativeStockKafkaDto = RelativeStockKafkaDto.of(stockNameArg, stockNumberArg);
            FinanceStockRedis financeStockRedis = crawlerService.financialCrawler(relativeStockKafkaDto.getStockName(), relativeStockKafkaDto.getStockNumber());
            recommendStockRedisRepo.saveStockRanking(financeStockRedis);
        } catch (IndexOutOfBoundsException e) {
            log.error("Index Error - findStockNumber 과정에서 주식 이름을 찾지 못해 에러가 발생했을 확률 높음");
        }
    }

    public ArrayList<FinanceStockRedis> extractBestStock() {
        return recommendStockRedisRepo.getStockRanking();
    }
}
