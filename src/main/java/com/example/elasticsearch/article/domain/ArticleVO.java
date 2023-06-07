package com.example.elasticsearch.article.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ArticleVO {
    private String title;
    private String analyzeResult;

    public ArticleVO(String title, String analyzeResult) {
        this.title = title;
        this.analyzeResult = analyzeResult;
    }

    public static ArticleVO of(String title, String analyzeResult) {
        return new ArticleVO(title, analyzeResult);
    }
}
