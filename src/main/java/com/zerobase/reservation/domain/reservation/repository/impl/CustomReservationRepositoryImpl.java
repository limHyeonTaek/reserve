package com.zerobase.reservation.domain.reservation.repository.impl;

import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.zerobase.reservation.domain.member.entity.Member;
import com.zerobase.reservation.domain.reservation.entity.Reservation;
import com.zerobase.reservation.domain.reservation.repository.CustomReservationRepository;
import com.zerobase.reservation.domain.reservation.type.ReservationType;
import com.zerobase.reservation.domain.store.entity.Store;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.zerobase.reservation.domain.member.entity.QMember.member;
import static com.zerobase.reservation.domain.reservation.entity.QReservation.reservation;
import static com.zerobase.reservation.domain.store.entity.QStore.store;

@RequiredArgsConstructor
public class CustomReservationRepositoryImpl implements CustomReservationRepository {
    private final JPAQueryFactory queryFactory;

    @Override
    public boolean existsReservation(Store store,
                                     LocalDate reservationDate,
                                     LocalTime reservationTime,
                                     ReservationType reservationType) {
        Reservation fetchOne = queryFactory
                .selectFrom(reservation)
                .where(
                        reservation.store.eq(store),
                        reservation.reservationDate.eq(reservationDate),
                        reservation.reservationTime.eq(reservationTime),
                        reservation.reservationType.eq(reservationType)
                )
                .fetchOne();

        return fetchOne != null;
    }

    @Override
    public Page<Reservation> findAllFetchJoin(
            Store store, LocalDate reservationDate, Pageable pageable) {
        List<OrderSpecifier> allOrders = getAllOrderSpecifier(pageable);

        List<Reservation> contents = queryFactory
                .selectFrom(reservation)
                .join(reservation.member, member).fetchJoin()
                .where(
                        reservation.store.eq(store),
                        reservation.reservationDate.eq(reservationDate)
                )
                .orderBy(allOrders.toArray(OrderSpecifier[]::new))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> countQuery = queryFactory
                .select(reservation.count())
                .from(reservation)
                .where(
                        reservation.store.eq(store),
                        reservation.reservationDate.eq(reservationDate)
                );

        return PageableExecutionUtils.getPage(contents, pageable,
                countQuery::fetchOne);
    }

    private List<OrderSpecifier> getAllOrderSpecifier(Pageable pageable) {
        List<OrderSpecifier> orders = new ArrayList<>();

        if (!pageable.getSort().isEmpty()) {
            pageable.getSort().forEach(order -> {
                Order direction = order.getDirection().isAscending()
                        ? Order.ASC : Order.DESC;

                PathBuilder orderExpression =
                        new PathBuilder<>(Reservation.class, "reservation");

                orders.add(new OrderSpecifier<>(direction,
                        orderExpression.get(order.getProperty())));
            });
        }

        return orders;
    }

    @Override
    public Optional<Reservation> findReservation(Member member,
                                                 Store store,
                                                 LocalDate reservationDate,
                                                 LocalTime reservationTime,
                                                 ReservationType reservationType) {
        return Optional.ofNullable(queryFactory
                .selectFrom(reservation)
                .where(
                        reservation.member.eq(member),
                        reservation.store.eq(store),
                        reservation.reservationDate.eq(reservationDate),
                        reservation.reservationTime.eq(reservationTime),
                        reservation.reservationType.eq(reservationType)
                )
                .orderBy(reservation.reservationTime.asc())
                .fetchOne());
    }

    @Override
    public Optional<Reservation> findByReservationKeyFetchJoin(String reservationKey) {
        return Optional.ofNullable(queryFactory
                .selectFrom(reservation)
                .join(reservation.member, member).fetchJoin()
                .join(reservation.store, store).fetchJoin()
                .where(reservation.reservationKey.eq(reservationKey))
                .fetchOne());
    }
}
