package com.zerobase.reservation.domain.reservation.entity;

import com.zerobase.reservation.domain.common.entity.BaseTimeEntity;
import com.zerobase.reservation.domain.member.entity.Member;
import com.zerobase.reservation.domain.reservation.converter.ReservationTypeConverter;
import com.zerobase.reservation.domain.reservation.converter.LocalTimeConverter;
import com.zerobase.reservation.domain.reservation.type.ReservationType;
import com.zerobase.reservation.domain.store.entity.Store;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(uniqueConstraints = {
        @UniqueConstraint(columnNames = {"store_id", "reservation_date", "reservation_time", "reservation_type"})
})
@Entity
public class Reservation extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String reservationKey;

    @Column(nullable = false, name = "reservation_date")
    private LocalDate reservationDate;

    @Column(nullable = false, name = "reservation_time")
    @Convert(converter = LocalTimeConverter.class)
    private LocalTime reservationTime;

    @Column(nullable = false)
    private Integer persons;

    @Column(nullable = false, name = "reservation_type")
    @Convert(converter = ReservationTypeConverter.class)
    private ReservationType reservationType;

    @Column(nullable = false)
    private boolean arrival = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id")
    private Store store;

    @Builder
    public Reservation(String reservationKey, LocalDate reservationDate, LocalTime reservationTime, Integer persons, ReservationType reservationType) {
        this.reservationKey = reservationKey;
        this.reservationDate = reservationDate;
        this.reservationTime = reservationTime;
        this.persons = persons;
        this.reservationType = reservationType;
    }

    public void addMemberAndStore(Member member, Store store) {
        this.member = member;
        this.store = store;
    }

    public void updateArrival() {
        this.arrival = true;
    }

    public void updateReservationType(ReservationType reservationType) {
        this.reservationType = reservationType;
    }
}
