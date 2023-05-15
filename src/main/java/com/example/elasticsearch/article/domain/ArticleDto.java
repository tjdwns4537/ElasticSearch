package com.example.elasticsearch.article.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Getter
@Setter
@Entity
public class ArticleDto {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String articleText;

    public ArticleDto() {

    }

    public ArticleDto(String text) {
        this.articleText = text;
    }

    public static ArticleDto of(String articleText) {
        return new ArticleDto(articleText);
    }
}
