package com.example.elasticsearch.search.service;

import com.example.elasticsearch.crawler.service.CrawlerService;
import com.example.elasticsearch.redis.repository.RecommendStockRedisRepo;
import com.example.elasticsearch.search.domain.Search;
import com.example.elasticsearch.search.repository.SearchRepository;
import com.example.elasticsearch.stock.domain.FinanceStockRedis;
import com.example.elasticsearch.stock.domain.StockElasticDto;
import com.example.elasticsearch.stock.service.StockService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jboss.jandex.Index;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    public void saveBestStock(List<StockElasticDto> stock) {
        for (StockElasticDto i : stock) {
            List<String> stockNumber = stockService.findStockNumber(i.getStockName());
            log.info("best stock stockNumber : {}", stockNumber);

            try {
                FinanceStockRedis financeStockRedis = crawlerService.financialCrawler(i.getStockName(), stockNumber.get(0));
                recommendStockRedisRepo.saveStockRanking(financeStockRedis);
            } catch (IndexOutOfBoundsException e) {
                FinanceStockRedis financeStockRedis = FinanceStockRedis.of(stockNumber.get(0), "x", "x", "x", "x", "x", "x", "x", "x");
                recommendStockRedisRepo.saveStockRanking(financeStockRedis);
                log.error("Index Error - findStockNumber 과정에서 주식 이름을 찾지 못해 에러가 발생했을 확률 높음");
            }

        }
    }

    public List<Object> extractBestStock() {
        return recommendStockRedisRepo.getStockRanking();
    }
}
