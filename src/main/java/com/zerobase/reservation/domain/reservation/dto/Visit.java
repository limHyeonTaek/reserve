package com.zerobase.reservation.domain.reservation.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalTime;

public class Visit {
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Request {
        @NotBlank
        @Pattern(regexp = "^\\d{3}-\\d{4}-\\d{4}$",
                message = "핸드폰 번호의 약식과 맞지 않습니다. ex) 010-1234-1234")
        private String phoneNumber;

        @NotBlank
        private String storeKey;

        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
        private LocalDate reservationDate;

        @DateTimeFormat(pattern = "HH:mm")
        private LocalTime reservationTime;
    }

    public record Response(
            String reservationKey
    ) {}
}
