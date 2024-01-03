package com.zerobase.reservation.domain.reservation.repository;

import com.zerobase.reservation.domain.reservation.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ReservationRepository extends JpaRepository<Reservation, Long>, CustomReservationRepository {
    Optional<Reservation> findByReservationKey(String reservationKey);
}
