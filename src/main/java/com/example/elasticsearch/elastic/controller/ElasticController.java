package com.example.elasticsearch.elastic.controller;

import com.example.elasticsearch.elastic.service.ArticleSearchService;
import com.example.elasticsearch.article.domain.Article;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Optional;

@Controller
@RequiredArgsConstructor
@Slf4j
public class ElasticController {

    @Autowired private final ArticleSearchService articleSearchService;

    @PostMapping(value = "/elastic")
    public ResponseEntity<HttpStatus> saveStock(@RequestBody Article article) { // 엘라스틱 저장소로 저장
        Optional<Article> save = Optional.ofNullable(articleSearchService.save(article));
        if(!save.isPresent()) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/elastic/{stockName}")
    public String findByIdStock(@PathVariable String stockName) {
//        log.info("주식 종목 명 : {}",stockName);
//
//        StockElasticDto stockElasticDto = new StockElasticDto(); // 반환할 ElasticDto
//
//        Optional<StockDbDto> stock = Optional.ofNullable(articleSearchService.findByName(stockName)); // DB에서 해당 종목이름의 데이터 출력
//        if(stock.isEmpty()) return "page404/notFound";
//
//        stockElasticDto.setStatusEnum(StatusEnum.OK);
//        stockElasticDto.setData(stock);
//        stockElasticDto.setMessage("해당 id의 데이터를 찾음");
//
//        HttpHeaders headers = new HttpHeaders(); // 응답 헤더
//        headers.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));

        return "redirect:/";
    }
}
