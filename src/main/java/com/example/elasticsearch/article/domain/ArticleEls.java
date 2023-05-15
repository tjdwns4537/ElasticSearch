package com.example.elasticsearch.article.domain;

import com.example.elasticsearch.helper.Indices;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.*;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@Document(indexName = Indices.ARTICLE_INDEX) // article 이라는 색인에 속함
public class ArticleEls {

    @Id
    @Field(type = FieldType.Keyword)
    private String id;

    /** 키워드 제목 */
    @Field(type = FieldType.Keyword)
    private String title;

    public ArticleEls(String title) {
        this.id = UUID.randomUUID().toString();
        this.title = title;
    }

    public static ArticleEls of(String title) {
        return new ArticleEls(title);
    }
}
