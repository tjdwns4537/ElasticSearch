package com.example.elasticsearch.stock.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QStockDbDto is a Querydsl query type for StockDbDto
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QStockDbDto extends EntityPathBase<StockDbDto> {

    private static final long serialVersionUID = 893262939L;

    public static final QStockDbDto stockDbDto = new QStockDbDto("stockDbDto");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath stockName = createString("stockName");

    public final StringPath stockPercent = createString("stockPercent");

    public final StringPath stockPrice = createString("stockPrice");

    public final StringPath tradeCount = createString("tradeCount");

    public QStockDbDto(String variable) {
        super(StockDbDto.class, forVariable(variable));
    }

    public QStockDbDto(Path<? extends StockDbDto> path) {
        super(path.getType(), path.getMetadata());
    }

    public QStockDbDto(PathMetadata metadata) {
        super(StockDbDto.class, metadata);
    }

}

