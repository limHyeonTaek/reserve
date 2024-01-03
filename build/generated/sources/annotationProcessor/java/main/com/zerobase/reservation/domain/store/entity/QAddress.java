package com.zerobase.reservation.domain.store.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QAddress is a Querydsl query type for Address
 */
@Generated("com.querydsl.codegen.DefaultEmbeddableSerializer")
public class QAddress extends BeanPath<Address> {

    private static final long serialVersionUID = -594699626L;

    public static final QAddress address1 = new QAddress("address1");

    public final StringPath address = createString("address");

    public final ComparablePath<org.locationtech.jts.geom.Point> coordinate = createComparable("coordinate", org.locationtech.jts.geom.Point.class);

    public final StringPath detailAddr = createString("detailAddr");

    public final StringPath zipcode = createString("zipcode");

    public QAddress(String variable) {
        super(Address.class, forVariable(variable));
    }

    public QAddress(Path<? extends Address> path) {
        super(path.getType(), path.getMetadata());
    }

    public QAddress(PathMetadata metadata) {
        super(Address.class, metadata);
    }

}

