package com.zerobase.reservation.domain.common.builder;

import com.zerobase.reservation.domain.reservation.entity.Reservation;

import static com.zerobase.reservation.domain.common.constants.ReservationConstants.*;

public class ReservationBuilder {
    public static Reservation reservation() {
        return Reservation.builder()
                .reservationKey(RESERVATION_KEY)
                .reservationDate(RESERVATION_DATE)
                .reservationTime(RESERVATION_TIME)
                .reservationType(RESERVATION_TYPE)
                .persons(PERSONS)
                .build();
    }
}
