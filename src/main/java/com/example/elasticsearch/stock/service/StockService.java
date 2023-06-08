package com.example.elasticsearch.stock.service;

import com.example.elasticsearch.crawler.repository.LikeStockJpaRepository;
import com.example.elasticsearch.crawler.repository.StockJpaRepository;
import com.example.elasticsearch.crawler.service.CrawlerService;
import com.example.elasticsearch.redis.repository.LikeStockRedisRepository;
import com.example.elasticsearch.redis.repository.LiveStockRedisRepository;
import com.example.elasticsearch.stock.domain.StockDbDto;
import com.example.elasticsearch.stock.domain.StockLikeDto;
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
public class StockService {

    @Autowired private final LikeStockJpaRepository likeStockJpaRepository;
    @Autowired private final CrawlerService crawlerService;
    @Autowired private final StockJpaRepository stockJpaRepository;
    @Autowired private final LikeStockRedisRepository likeStockRedisRepository;
    @Autowired private final LiveStockRedisRepository liveStockRedisRepository;

    public List<String> findStockNumber(String stockName) {
        return crawlerService.findStockNumber(stockName);
    }

    public void deleteLikeStock(String stockName) {
        String stockNumber = crawlerService.findStockNumber(stockName).get(0); // 주식명으로 주식넘버 조회
        if(stockNumber.isEmpty()) return;

        StockLikeDto byLikeStockName = likeStockJpaRepository.findByLikeStockName(stockNumber); //관심목록 리스트 삭제
        likeStockJpaRepository.deleteById(byLikeStockName.getId());

        StockDbDto byStockName = stockJpaRepository.findByStockName(stockName); //관심목록 상세리스트 삭제
        stockJpaRepository.deleteById(byStockName.getId());

        likeStockRedisRepository.deleteLikeStock(stockName); // 실시간 레디스 목록 삭제
    }

    public String saveStockNumber(String stockName) {
        String stockNumber = "";
        try {
            Optional<StockLikeDto> byLikeStockName = Optional.of(likeStockJpaRepository.findByLikeStockName(stockName));
            likeStockJpaRepository.deleteById(byLikeStockName.get().getId());
            stockNumber = crawlerService.findStockNumber(stockName).get(0); // 주식명으로 주식넘버 조회
            likeStockJpaRepository.save(StockLikeDto.of(stockNumber));
        } catch (NullPointerException e) {
            stockNumber = crawlerService.findStockNumber(stockName).get(0); // 주식명으로 주식넘버 조회
            likeStockJpaRepository.save(StockLikeDto.of(stockNumber));
        }

        return stockNumber;
    }

    public List<String> getLikeStockAll() {
        List<StockDbDto> all = stockJpaRepository.findAll();
        List<String> list = new ArrayList<>();
        for (StockDbDto i : all) {
            list.add(printStock(i));
        }
        return list;
    }

    public List<String> getLikeStockRanking() {
        List<String> likeStockRanking = likeStockRedisRepository.getLikeStockRanking();
        List<String> list = new ArrayList<>();

        if(likeStockRanking.isEmpty()) return list;
        for (String i : likeStockRanking) {
            StockDbDto byStockName = stockJpaRepository.findByStockName(i);
            list.add(printStock(byStockName));
        }
        return list;
    }

    public List<String> getStockLive() {
        return liveStockRedisRepository.getStockLive();
    }

    public String printStock(StockDbDto stockDbDto) {
        String result = "["+stockDbDto.getStockName() + "] 현재가: "
                +stockDbDto.getStockPrice()+"원 - 등락율 : "
                +stockDbDto.getStockPercent() + "%";
        return result;
    }
}
