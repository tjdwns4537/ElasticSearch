package com.example.elasticsearch.elastic.controller;

import com.example.elasticsearch.stock.domain.Stock;
import com.example.elasticsearch.stock.domain.StockDbDto;
import com.example.elasticsearch.stock.domain.StockElasticDto;
import com.example.elasticsearch.helper.StatusEnum;
import com.example.elasticsearch.elastic.service.StockSearchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.Charset;
import java.util.Optional;

@RestController(value = "/elastic")
@RequiredArgsConstructor
@Slf4j
public class ElasticController {

    @Autowired private final StockSearchService stockSearchService;

    @PostMapping
    public ResponseEntity<HttpStatus> saveStock(@RequestBody Stock stock) {
        Optional<Stock> save = Optional.ofNullable(stockSearchService.save(stock));
        if(!save.isPresent()) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/extract/{stockName}")
    public ResponseEntity<StockElasticDto> findByIdStock(@PathVariable String stockName) {
        log.info("주식 종목 명 : {}",stockName);

        StockElasticDto stockElasticDto = new StockElasticDto();

        Optional<StockDbDto> stock = Optional.ofNullable(stockSearchService.findByName(stockName)); // DB에서 해당 종목이름의 데이터 출력
        if(stock.isEmpty()) return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));

        stockElasticDto.setStatusEnum(StatusEnum.OK);
        stockElasticDto.setData(stock);
        stockElasticDto.setMessage("해당 id의 데이터를 찾음");

        return new ResponseEntity<>(stockElasticDto, headers ,HttpStatus.OK);
    }
}
