package com.zerobase.reservation.domain.reservation.dto;

import com.zerobase.reservation.domain.reservation.entity.Reservation;
import com.zerobase.reservation.domain.reservation.type.ReservationType;
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
public class ReservationsResponse {
    private String reservationKey;
    private LocalDate reservationDate;
    private LocalTime reservationTime;
    private Integer persons;
    private ReservationType reservationType;
    private String memberName;
    private String memberPhoneNumber;

    public static ReservationsResponse fromEntity(Reservation reservation) {
        return ReservationsResponse.builder()
                .reservationKey(reservation.getReservationKey())
                .reservationDate(reservation.getReservationDate())
                .reservationTime(reservation.getReservationTime())
                .persons(reservation.getPersons())
                .reservationType(reservation.getReservationType())
                .memberName(reservation.getMember().getName())
                .memberPhoneNumber(reservation.getMember().getPhoneNumber())
                .build();
    }
}
