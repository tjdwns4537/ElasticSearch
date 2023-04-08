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
public class Stock {
    @Id
    @Field(type = FieldType.Keyword)
    private Long id;

    /** 종목 이름 */
    @Field(type = FieldType.Text)
    private String title;

    /** 종목 가격 */
    @Field(type = FieldType.Text)
    private String price;

    /** 종목 등락율 */
    @Field(type = FieldType.Text)
    private String percent;
}
