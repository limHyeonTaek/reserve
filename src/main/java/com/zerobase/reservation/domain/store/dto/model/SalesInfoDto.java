package com.zerobase.reservation.domain.store.dto.model;

import com.zerobase.reservation.domain.store.entity.SalesInfo;
import jakarta.validation.constraints.NotNull;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalTime;
import java.util.List;

public record SalesInfoDto(
        @NotNull
        @DateTimeFormat(pattern = "HH:mm")
        LocalTime operatingStart,

        @NotNull
        @DateTimeFormat(pattern = "HH:mm")
        LocalTime operatingEnd,

        List<String> closedDays
) {

    public static SalesInfo toEntity(SalesInfoDto salesInfoDto) {
        return SalesInfo.builder()
                .operatingStart(salesInfoDto.operatingStart)
                .operatingEnd(salesInfoDto.operatingEnd)
                .closedDays(salesInfoDto.closedDays)
                .build();
    }

    public static SalesInfoDto fromEntity(SalesInfo salesInfo) {
        return new SalesInfoDto(salesInfo.getOperatingStart(),
                salesInfo.getOperatingEnd(), salesInfo.getClosedDays());
    }
}
