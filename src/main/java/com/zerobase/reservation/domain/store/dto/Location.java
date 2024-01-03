package com.zerobase.reservation.domain.store.dto;

import jakarta.validation.constraints.NotNull;

public record Location(
        @NotNull
        Double x, // 경도

        @NotNull
        Double y // 위도
) {
}
