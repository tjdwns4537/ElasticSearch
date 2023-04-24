package com.example.elasticsearch.stock.domain;

import com.example.elasticsearch.helper.Indices;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Document(indexName = Indices.STOCK_INDEX) // article 이라는 색인에 속함
@Mapping(mappingPath = "/static/elastic/article-mapping.json")
@Setting(settingPath = "/static/elastic/article-setting.json")
public class Article {
    @Id
    @Field(type = FieldType.Keyword)
    private Long id;

    /** 기사 키워드 이름 */
    @Field(type = FieldType.Text)
    private String articleName;

    /** 기사 날짜 */
    @Field(type = FieldType.Text)
    private String articleDate;

    /** 긍정/부정 상태 */
    @Field(type = FieldType.Text)
    private String state;
}
