package com.zerobase.reservation.domain.reservation.api;

import com.zerobase.reservation.domain.reservation.dto.Confirm;
import com.zerobase.reservation.domain.reservation.dto.ReservationsResponse;
import com.zerobase.reservation.domain.reservation.dto.Reserve;
import com.zerobase.reservation.domain.reservation.dto.Visit;
import com.zerobase.reservation.domain.reservation.service.ReservationService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

import static org.springframework.format.annotation.DateTimeFormat.ISO;

@RequestMapping("/api/reservation")
@RequiredArgsConstructor
@RestController
public class ReservationController {
    private final ReservationService reservationService;

    /**
     매장 예약
     */
    @PostMapping
    public ResponseEntity<Reserve.Response> reserve(
            @RequestBody @Valid Reserve.Request request) {
        return ResponseEntity.ok(
                Reserve.Response.from(reservationService.reserve(request)));
    }

    /**
     날짜별 예약 조회
     */
    @GetMapping
    public ResponseEntity<Page<ReservationsResponse>> reservations(
            @RequestParam @NotBlank String storeKey,
            @RequestParam @NotNull @DateTimeFormat(iso = ISO.DATE)
            LocalDate reservationDate,
            @PageableDefault(sort = "reservationTime", direction = Direction.ASC)
            final Pageable pageable
    ) {
        return ResponseEntity.ok(reservationService.reservations(
                storeKey, reservationDate, pageable));
    }

    /**
     예약 확인 (승인/취소)
     매니저 권한이 있는 유저만 접근할 수 있음.
     */
    @PreAuthorize("hasRole('MANAGER')")
    @PatchMapping("/confirm")
    public ResponseEntity<Confirm.Response> confirm(
            @RequestBody @Valid Confirm.Request request) {
        return ResponseEntity.ok(reservationService.confirm(request));
    }

    /**
     방문 확인
     매장의 키오스크를 통하기 때문에 인증 없이 접근할 수 있습니다.
     */
    @PatchMapping("/visit")
    public ResponseEntity<?> visit(@RequestBody @Valid Visit.Request request) {
        return ResponseEntity.ok(reservationService.visit(request));
    }
}
