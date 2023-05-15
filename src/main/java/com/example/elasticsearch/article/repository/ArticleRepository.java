package com.example.elasticsearch.article.repository;

import com.example.elasticsearch.article.domain.ArticleDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArticleRepository extends JpaRepository<ArticleDto, String> {
}
