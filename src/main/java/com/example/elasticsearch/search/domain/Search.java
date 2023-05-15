package com.example.elasticsearch.search.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@Setter
@Entity
public class Search {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String keyword;
    private Integer positiveNumber;
    private Integer negativeNumber;
    private String searchDate;

    public Search() {

    }

    public Search(String keyword) {
        ZonedDateTime nowSeoul = ZonedDateTime.now(ZoneId.of("Asia/Seoul"));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        String format = nowSeoul.format(formatter);

        this.keyword = keyword;
        this.positiveNumber = 0;
        this.negativeNumber = 0;
        this.searchDate = format;
    }

    public static Search of(String keyword) {
        return new Search(keyword);
    }
}
