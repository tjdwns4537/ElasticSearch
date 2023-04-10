package com.example.elasticsearch.elastic.controller;

import com.example.elasticsearch.stock.domain.Stock;
import com.example.elasticsearch.stock.domain.StockElasticDto;
import com.example.elasticsearch.helper.StatusEnum;
import com.example.elasticsearch.stock.service.StockSearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.Charset;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class ElasticController {

    @Autowired private final StockSearchService stockSearchService;

    @PostMapping
    public ResponseEntity<HttpStatus> saveStock(@RequestBody Stock stock) {
        Optional<Stock> save = Optional.ofNullable(stockSearchService.save(stock));
        if(!save.isPresent()) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<StockElasticDto> findByIdStock(@PathVariable String id) {
        StockElasticDto stockElasticDto = new StockElasticDto();

        Optional<Stock> articleDoc = Optional.ofNullable(stockSearchService.findById(id));
        if(articleDoc.isEmpty()) return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));

        stockElasticDto.setStatusEnum(StatusEnum.OK);
        stockElasticDto.setData(articleDoc);
        stockElasticDto.setMessage("해당 id의 데이터를 찾음");

        return new ResponseEntity<>(stockElasticDto, headers ,HttpStatus.OK);
    }
}
