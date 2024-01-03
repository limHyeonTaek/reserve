package com.zerobase.reservation.domain.store.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QSalesInfo is a Querydsl query type for SalesInfo
 */
@Generated("com.querydsl.codegen.DefaultEmbeddableSerializer")
public class QSalesInfo extends BeanPath<SalesInfo> {

    private static final long serialVersionUID = -1234003460L;

    public static final QSalesInfo salesInfo = new QSalesInfo("salesInfo");

    public final ListPath<String, StringPath> closedDays = this.<String, StringPath>createList("closedDays", String.class, StringPath.class, PathInits.DIRECT2);

    public final TimePath<java.time.LocalTime> operatingEnd = createTime("operatingEnd", java.time.LocalTime.class);

    public final TimePath<java.time.LocalTime> operatingStart = createTime("operatingStart", java.time.LocalTime.class);

    public QSalesInfo(String variable) {
        super(SalesInfo.class, forVariable(variable));
    }

    public QSalesInfo(Path<? extends SalesInfo> path) {
        super(path.getType(), path.getMetadata());
    }

    public QSalesInfo(PathMetadata metadata) {
        super(SalesInfo.class, metadata);
    }

}

