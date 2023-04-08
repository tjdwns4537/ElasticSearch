package com.example.elasticsearch.controller;

import com.example.elasticsearch.document.ArticleDoc;
import com.example.elasticsearch.domain.ArticleDto;
import com.example.elasticsearch.helper.StatusEnum;
import com.example.elasticsearch.service.ArticleSearchService;
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

    @Autowired private final ArticleSearchService articleSearchService;

    @PostMapping
    public ResponseEntity<HttpStatus> saveArticle(@RequestBody ArticleDoc articleDoc) {
        Optional<ArticleDoc> save = Optional.ofNullable(articleSearchService.save(articleDoc));
        if(!save.isPresent()) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ArticleDto> findByIdArticle(@PathVariable String id) {
        ArticleDto articleDto = new ArticleDto();

        Optional<ArticleDoc> articleDoc = Optional.ofNullable(articleSearchService.findById(id));
        if(articleDoc.isEmpty()) return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));

        articleDto.setStatusEnum(StatusEnum.OK);
        articleDto.setData(articleDoc);
        articleDto.setMessage("해당 id의 데이터를 찾음");

        return new ResponseEntity<>(articleDto, headers ,HttpStatus.OK);
    }
}
