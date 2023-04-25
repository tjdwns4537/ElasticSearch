package com.example.elasticsearch.stock.domain;

import com.example.elasticsearch.helper.Indices;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.elasticsearch.annotations.*;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import java.util.Date;

@TypeAlias("article")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Document(indexName = Indices.ARTICLE_INDEX) // article 이라는 색인에 속함
//@Mapping(mappingPath = "/static/elastic/article-mapping.json")
//@Setting(settingPath = "/static/elastic/article-setting.json")
public class Article {

    @Id
    @Field(type = FieldType.Long)
    private Long id;

    /** 키워드 제목 */
    @Field(type = FieldType.Text)
    private String title;

    public Article(String title) {
        this.title = title;
    }

    public static Article of(String title) {
        return new Article(title);
    }

//    /** 키워드 */
//    @Field(type = FieldType.Text)
//    private String searchKeyword;

//    /** 긍정/부정 상태 */
//    @Field(type = FieldType.Text)
//    private String state;

//    @Field(type = FieldType.Text)
//    private Integer count;

//    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyymmdd")
//    @Field(type = FieldType.Long, format = DateFormat.basic_date)
//    private Date readDate;
}
