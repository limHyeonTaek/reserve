package com.zerobase.reservation.domain.reservation.repository;

import com.zerobase.reservation.domain.member.entity.Member;
import com.zerobase.reservation.domain.reservation.entity.Reservation;
import com.zerobase.reservation.domain.reservation.type.ReservationType;
import com.zerobase.reservation.domain.store.entity.Store;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;

public interface CustomReservationRepository {
    /**
     해당 예약이 있는지 확인하는 쿼리입니다.
     */
    boolean existsReservation(Store store,
                              LocalDate reservationDate,
                              LocalTime reservationTime,
                              ReservationType reservationType);

    Page<Reservation> findAllFetchJoin(
            Store store, LocalDate reservationDate, Pageable pageable);

    /**
     해당 예약을 조회하는 쿼리입니다.
     */
    Optional<Reservation> findReservation(Member member,
                                          Store store,
                                          LocalDate reservationDate,
                                          LocalTime reservationTime,
                                          ReservationType reservationType);

    Optional<Reservation> findByReservationKeyFetchJoin(String reservationKey);
}
