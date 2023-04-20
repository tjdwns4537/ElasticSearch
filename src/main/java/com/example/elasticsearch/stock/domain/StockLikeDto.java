package com.example.elasticsearch.stock.domain;

import lombok.Getter;
import org.springframework.web.bind.annotation.GetMapping;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@Table(name = "stocks")
public class StockLikeDto {

    @Id @GeneratedValue
    private Long id;

    @Column(name = "likeList")
    private String likeStock;

    public StockLikeDto() {

    }


    public StockLikeDto(String likeStock) {
        this.likeStock = likeStock;
    }


    public static StockLikeDto of(String likeStock) {
        return new StockLikeDto(likeStock);
    }
}
