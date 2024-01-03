package com.zerobase.reservation.domain.store.entity;

import com.zerobase.reservation.domain.store.converter.ListConverter;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalTime;
import java.util.List;

/**
 영업 정보 값타입 엔티티
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Embeddable
public class SalesInfo {
    @Column(name = "oper_start", nullable = false)
    private LocalTime operatingStart;

    @Column(name = "oper_end", nullable = false)
    private LocalTime operatingEnd;

    @Convert(converter = ListConverter.class)
    private List<String> closedDays;

    @Builder
    public SalesInfo(LocalTime operatingStart, LocalTime operatingEnd, List<String> closedDays) {
        this.operatingStart = operatingStart;
        this.operatingEnd = operatingEnd;
        this.closedDays = closedDays;
    }
}
