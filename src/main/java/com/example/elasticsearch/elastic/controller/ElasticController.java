package com.example.elasticsearch.elastic.controller;

import com.example.elasticsearch.elastic.service.ElasticService;
import com.example.elasticsearch.article.domain.ArticleEls;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Optional;

@Controller
@RequiredArgsConstructor
@Slf4j
public class ElasticController {

    @Autowired private final ElasticService elasticService;

    @PostMapping(value = "/elastic")
    public ResponseEntity<HttpStatus> saveStock(@RequestBody ArticleEls articleEls) { // 엘라스틱 저장소로 저장
        Optional<ArticleEls> save = Optional.ofNullable(elasticService.save(articleEls));
        if(!save.isPresent()) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
