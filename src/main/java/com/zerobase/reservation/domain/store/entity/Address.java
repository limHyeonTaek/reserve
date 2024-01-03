package com.zerobase.reservation.domain.store.entity;

import com.zerobase.reservation.domain.store.type.SRID;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.PrecisionModel;

/**
 주소 값타입 엔티티
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Embeddable
public class Address {
    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private String detailAddr;

    @Column(nullable = false)
    private String zipcode;

    @Column(columnDefinition = "POINT SRID 4326", nullable = false)
    private Point coordinate;

    @Builder
    public Address(String address, String detailAddr, String zipcode) {
        this.address = address;
        this.detailAddr = detailAddr;
        this.zipcode = zipcode;
    }

    public void addCoordinate(Double x, Double y) {
        GeometryFactory geometryFactory = new GeometryFactory(
                new PrecisionModel(), SRID.POINT.getKey());
        this.coordinate = geometryFactory.createPoint(new Coordinate(x, y));
    }
}
