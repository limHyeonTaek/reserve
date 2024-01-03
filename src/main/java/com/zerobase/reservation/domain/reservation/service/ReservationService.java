package com.zerobase.reservation.domain.reservation.service;

import com.zerobase.reservation.domain.common.utils.KeyGenerator;
import com.zerobase.reservation.domain.member.entity.Member;
import com.zerobase.reservation.domain.member.service.MemberService;
import com.zerobase.reservation.domain.reservation.dto.Confirm;
import com.zerobase.reservation.domain.reservation.dto.ReservationsResponse;
import com.zerobase.reservation.domain.reservation.dto.Reserve;
import com.zerobase.reservation.domain.reservation.dto.Visit;
import com.zerobase.reservation.domain.reservation.dto.model.ReservationDto;
import com.zerobase.reservation.domain.reservation.entity.Reservation;
import com.zerobase.reservation.domain.reservation.exception.ReservationException;
import com.zerobase.reservation.domain.reservation.repository.ReservationRepository;
import com.zerobase.reservation.domain.reservation.type.ReservationType;
import com.zerobase.reservation.domain.store.entity.Store;
import com.zerobase.reservation.domain.store.service.StoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;

import static com.zerobase.reservation.general.exception.ErrorCode.*;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class ReservationService {
    private final ReservationRepository reservationRepository;
    private final MemberService memberService;
    private final StoreService storeService;
    private final KeyGenerator keyGenerator;

    /**
     매장 예약
     매장 조회 후 해당 매장에 요청한 예약이 가능한지 확인하고, 예약을 진행
     */
    @Transactional
    public ReservationDto reserve(Reserve.Request request) {
        Store store = storeService.findByStoreKeyOrThrow(request.getStoreKey());

        validateReservationExists(request.getReservationDate(),
                request.getReservationTime(), store);

        Member member = memberService.findByMemberKeyOrThrow(request.getMemberKey());

        return ReservationDto.fromEntity(reservationRepository.save(
                request.toEntity(keyGenerator.generateKey(), member, store)));
    }

    private void validateReservationExists(LocalDate reservationDate,
                                           LocalTime reservationTime,
                                           Store store) {
        if (reservationRepository.existsReservation(store, reservationDate,
                reservationTime, ReservationType.WAIT)) {
            throw new ReservationException(ALREADY_RESERVED);
        }
    }

    /**
     날짜별 예약 조회
     연관관계(member, store)를 함께 조회하기 위해 fetch join을 사용
     */
    public Page<ReservationsResponse> reservations(String storeKey,
                                                   LocalDate reservationDate,
                                                   Pageable pageable) {
        Store store = storeService.findByStoreKeyOrThrow(storeKey);

        return reservationRepository.findAllFetchJoin(
                        store, reservationDate, pageable)
                .map(ReservationsResponse::fromEntity);
    }

    /**
     예약 확인 (승인/취소)
     점주가 요청한 예약 타입으로 변경
     */
    @Transactional
    public Confirm.Response confirm(Confirm.Request request) {
        Reservation reservation =
                reservationRepository.findByReservationKey(request.reservationKey())
                        .orElseThrow(() ->
                                new ReservationException(RESERVATION_NOT_FOUND));

        reservation.updateReservationType(request.reservationType());
        return new Confirm.Response(reservation.getReservationKey());
    }

    /**
     방문 확인
     요청 정보를 통해 찾은 회원, 매장 정보를 포함하여 승인된 예약이 있는지 조회
     예약이 존재한다면 도착 시간이 지났는지 확인합니다. 에약 시간 10분 전이라면 방문 확인에 성공
     */
    @Transactional
    public Visit.Response visit(Visit.Request request) {
        Member member = memberService.findByPhoneNumberOrThrow(
                request.getPhoneNumber());
        Store store = storeService.findByStoreKeyOrThrow(request.getStoreKey());

        Reservation reservation =
                reservationRepository.findReservation(member, store,
                                request.getReservationDate(),
                                request.getReservationTime(),
                                ReservationType.CONFIRM)
                        .orElseThrow(() ->
                                new ReservationException(RESERVATION_NOT_FOUND));

        validateVisit(reservation);

        reservation.updateArrival();
        return new Visit.Response(reservation.getReservationKey());
    }

    private static void validateVisit(Reservation reservation) {
        if (LocalTime.now().isAfter(reservation.getReservationTime()
                .minus(Duration.ofMinutes(10)))) {
            throw new ReservationException(ARRIVAL_TIME_EXCEED);
        }

        if (reservation.isArrival()) {
            throw new ReservationException(ALREADY_ARRIVAL);
        }
    }

    public Reservation getReservationFetchJoinOrThrow(String reservationKey) {
        return reservationRepository.findByReservationKeyFetchJoin(reservationKey)
                .orElseThrow(() ->
                        new ReservationException(RESERVATION_NOT_FOUND));
    }
}
