package com.example.elasticsearch.domain;

import com.example.elasticsearch.document.ArticleDoc;

import java.util.List;

public interface PerformanceSearchUseCase {
    void saveAllDocuments();
    List<ArticleDoc> searchByTitle(String title);
    List<ArticleDoc> searchByPlace(String place);
    List<ArticleDoc> searchByPerformanceType(String type);
}
