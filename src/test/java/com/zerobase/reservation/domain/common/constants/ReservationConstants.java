package com.zerobase.reservation.domain.common.constants;

import com.zerobase.reservation.domain.reservation.type.ReservationType;

import java.time.LocalDate;
import java.time.LocalTime;

public class ReservationConstants {
    public static final String RESERVATION_KEY = "5d7908f0b3a541699cea16e7c5c9b5b4";
    public static final LocalDate RESERVATION_DATE = LocalDate.of(2023, 12, 26);
    public static final LocalTime RESERVATION_TIME = LocalTime.of(11, 0);
    public static final String RESERVATION_TIME_STR = "11:00:00";
    public static final Integer PERSONS = 2;
    public static final ReservationType RESERVATION_TYPE = ReservationType.WAIT;

}
