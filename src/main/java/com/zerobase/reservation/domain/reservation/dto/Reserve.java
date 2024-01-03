package com.zerobase.reservation.domain.reservation.dto;

import com.zerobase.reservation.domain.member.entity.Member;
import com.zerobase.reservation.domain.reservation.dto.model.ReservationDto;
import com.zerobase.reservation.domain.reservation.entity.Reservation;
import com.zerobase.reservation.domain.reservation.type.ReservationType;
import com.zerobase.reservation.domain.store.entity.Store;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

import java.time.LocalDate;
import java.time.LocalTime;

public class Reserve {
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Request {
        @NotBlank
        private String memberKey;

        @NotBlank
        private String storeKey;

        @DateTimeFormat(iso = ISO.DATE)
        private LocalDate reservationDate;

        @DateTimeFormat(pattern = "HH:mm")
        private LocalTime reservationTime;

        private Integer persons;

        public Reservation toEntity(String reservationKey, Member member, Store store) {
            Reservation reservation = Reservation.builder()
                    .reservationKey(reservationKey)
                    .reservationDate(reservationDate)
                    .reservationTime(reservationTime)
                    .persons(persons)
                    .reservationType(ReservationType.WAIT)
                    .build();

            reservation.addMemberAndStore(member, store);
            return reservation;
        }
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Response {
        private String storeName;
        private String storeKey;
        private LocalDate reservationDate;
        private LocalTime reservationTime;
        private Integer persons;
        private ReservationType reservationType;

        public static Response from(ReservationDto reservationDto) {
            return Response.builder()
                    .storeName(reservationDto.getStore().getName())
                    .storeKey(reservationDto.getStore().getStoreKey())
                    .reservationDate(reservationDto.getReservationDate())
                    .reservationTime(reservationDto.getReservationTime())
                    .persons(reservationDto.getPersons())
                    .reservationType(reservationDto.getReservationType())
                    .build();
        }
    }
}
