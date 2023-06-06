package com.example.elasticsearch.search.domain;

import com.example.elasticsearch.stock.domain.StockElasticDto;
import com.example.elasticsearch.thema.domain.Thema;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class AnalyzeResultSearch {
    String searchInfo;
    List<String> articleList = new ArrayList<>();
    Integer positive;
    Integer negative;
    List<Thema> themaList = new ArrayList<>();
    List<StockElasticDto> relativeList = new ArrayList<>();

    public AnalyzeResultSearch() {

    }

    public AnalyzeResultSearch(String searchInfo, List<String> articleList, Integer positive, Integer negative, List<Thema> themaList, List<StockElasticDto> relativeList) {
        this.searchInfo = searchInfo;
        this.articleList = articleList;
        this.positive = positive;
        this.negative = negative;
        this.themaList = themaList;
        this.relativeList = relativeList;
    }

    public static AnalyzeResultSearch of(String searchInfo, List<String> articleList, Integer positive, Integer negative, List<Thema> themaList, List<StockElasticDto> relativeList) {
        return new AnalyzeResultSearch(searchInfo, articleList, positive, negative, themaList, relativeList);
    }
}
