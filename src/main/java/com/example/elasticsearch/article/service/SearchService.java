package com.example.elasticsearch.article.service;

import com.example.elasticsearch.article.domain.Search;
import com.example.elasticsearch.article.repository.SearchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SearchService {

    @Autowired
    private final SearchRepository searchRepository;

    public void save(Search search) {
        searchRepository.save(search);
    }

    public Optional<Search> findById(Long id) {
        return searchRepository.findById(id);
    }

    public List<Search> findByAll() {
       return searchRepository.findAll();
    }
}
