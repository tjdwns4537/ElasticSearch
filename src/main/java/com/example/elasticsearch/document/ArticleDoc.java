package com.example.elasticsearch.document;

import com.example.elasticsearch.helper.Indices;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Document(indexName = Indices.ARTICLE_INDEX) // article 이라는 색인에 속함
@Mapping(mappingPath = "elastic/article-mapping.json")
@Setting(settingPath = "elastic/article-setting.json")
public class ArticleDoc {
    @Id
    @Field(type = FieldType.Keyword)
    private Long id;
    /** 제목 */
    @Field(type = FieldType.Text)
    private String title;
    /** 기사 날짜 */
    @Field(type = FieldType.Text)
    private String articleDateTime;
    /** 키워드 */
    @Field(type = FieldType.Text)
    private String keyword;
}
