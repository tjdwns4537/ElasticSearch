package com.example.elasticsearch.stock.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QStockLikeDto is a Querydsl query type for StockLikeDto
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QStockLikeDto extends EntityPathBase<StockLikeDto> {

    private static final long serialVersionUID = -1821743070L;

    public static final QStockLikeDto stockLikeDto = new QStockLikeDto("stockLikeDto");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath likeStock = createString("likeStock");

    public QStockLikeDto(String variable) {
        super(StockLikeDto.class, forVariable(variable));
    }

    public QStockLikeDto(Path<? extends StockLikeDto> path) {
        super(path.getType(), path.getMetadata());
    }

    public QStockLikeDto(PathMetadata metadata) {
        super(StockLikeDto.class, metadata);
    }

}

