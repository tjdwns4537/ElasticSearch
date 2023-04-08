package com.example.elasticsearch.article.domain;

import com.example.elasticsearch.helper.StatusEnum;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class ArticleDto {
    private StatusEnum statusEnum;
    private String message;
    private Object data;

    public ArticleDto() {
        this.statusEnum = StatusEnum.BAD_REQUEST;
        this.message = null;
        this.data = null;
    }
}
