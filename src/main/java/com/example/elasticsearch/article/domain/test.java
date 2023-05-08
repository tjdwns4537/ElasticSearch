package com.example.elasticsearch.article.domain;

import com.example.elasticsearch.helper.Indices;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Getter
@Setter
@Document(indexName = Indices.ARTICLE_INDEX)
public class test {
    @Id
    private String id;

    @Field(type = FieldType.Text, analyzer = "sentiment")
    private String myField;
}
