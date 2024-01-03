package com.zerobase.reservation.domain.reservation.dto.model;

import com.zerobase.reservation.domain.member.entity.Member;
import com.zerobase.reservation.domain.reservation.entity.Reservation;
import com.zerobase.reservation.domain.reservation.type.ReservationType;
import com.zerobase.reservation.domain.store.entity.Store;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReservationDto {
    private String reservationKey;
    private LocalDate reservationDate;
    private LocalTime reservationTime;
    private Integer persons;
    private ReservationType reservationType;
    private boolean arrival;
    private Store store;
    private Member member;

    public static ReservationDto fromEntity(Reservation reservation) {
        return ReservationDto.builder()
                .reservationKey(reservation.getReservationKey())
                .reservationDate(reservation.getReservationDate())
                .reservationTime(reservation.getReservationTime())
                .persons(reservation.getPersons())
                .reservationType(reservation.getReservationType())
                .arrival(reservation.isArrival())
                .store(reservation.getStore())
                .member(reservation.getMember())
                .build();
    }
}
