package com.zerobase.reservation.domain.common.builder.dto;

import com.zerobase.reservation.domain.common.builder.MemberBuilder;
import com.zerobase.reservation.domain.common.builder.StoreBuilder;
import com.zerobase.reservation.domain.reservation.dto.model.ReservationDto;

import static com.zerobase.reservation.domain.common.constants.ReservationConstants.*;
import static com.zerobase.reservation.domain.common.constants.ReservationConstants.RESERVATION_TYPE;

public class ReservationDtoBuilder {
    public static ReservationDto reservationDto() {
        return ReservationDto.builder()
                .reservationKey(RESERVATION_KEY)
                .reservationDate(RESERVATION_DATE)
                .reservationTime(RESERVATION_TIME)
                .reservationType(RESERVATION_TYPE)
                .arrival(false)
                .persons(PERSONS)
                .store(StoreBuilder.store())
                .member(MemberBuilder.member())
                .build();
    }
}
