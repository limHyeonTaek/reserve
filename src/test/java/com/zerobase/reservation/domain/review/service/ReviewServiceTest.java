package com.zerobase.reservation.domain.review.service;

import com.zerobase.reservation.domain.common.builder.MemberBuilder;
import com.zerobase.reservation.domain.common.builder.ReservationBuilder;
import com.zerobase.reservation.domain.common.builder.ReviewBuilder;
import com.zerobase.reservation.domain.common.builder.StoreBuilder;
import com.zerobase.reservation.domain.member.entity.Member;
import com.zerobase.reservation.domain.member.entity.Role;
import com.zerobase.reservation.domain.member.service.MemberService;
import com.zerobase.reservation.domain.reservation.entity.Reservation;
import com.zerobase.reservation.domain.reservation.exception.ReservationException;
import com.zerobase.reservation.domain.reservation.service.ReservationService;
import com.zerobase.reservation.domain.reservation.type.ReservationType;
import com.zerobase.reservation.domain.review.dto.Update;
import com.zerobase.reservation.domain.review.dto.Write;
import com.zerobase.reservation.domain.review.dto.model.ReviewDto;
import com.zerobase.reservation.domain.review.entity.Review;
import com.zerobase.reservation.domain.review.exception.ReviewException;
import com.zerobase.reservation.domain.review.repository.ReviewRepository;
import com.zerobase.reservation.domain.store.entity.Store;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static com.zerobase.reservation.domain.common.constants.ReviewConstants.*;
import static com.zerobase.reservation.general.exception.ErrorCode.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;

@ExtendWith(MockitoExtension.class)
class ReviewServiceTest {
    @Mock
    ReviewRepository reviewRepository;

    @Mock
    ReservationService reservationService;

    @Mock
    MemberService memberService;

    @InjectMocks
    ReviewService reviewService;

    @Test
    @DisplayName("리뷰 작성 성공")
    void write_success() {
        // given
        Member member = MemberBuilder.member();
        Store store = StoreBuilder.store();

        Reservation reservation = ReservationBuilder.reservation();
        reservation.addMemberAndStore(member, store);
        reservation.updateReservationType(ReservationType.CONFIRM);
        reservation.updateArrival();

        given(reservationService.getReservationFetchJoinOrThrow(any()))
                .willReturn(reservation);

        Review review = ReviewBuilder.review();
        review.addMemberAndStore(member, store);

        given(reviewRepository.save(any()))
                .willReturn(review);

        // when
        ReviewDto reviewDto = reviewService.write(
                Write.Request.builder().build(), MemberBuilder.member());

        // then
        assertEquals(CONTENTS, reviewDto.getContents());
        assertEquals(SCORE, reviewDto.getScore());
    }

    @Test
    @DisplayName("리뷰 작성 실패 - 존재하지 않는 예약")
    void write_reservation_not_found() {
        // given
        given(reservationService.getReservationFetchJoinOrThrow(any()))
                .willThrow(new ReservationException(RESERVATION_NOT_FOUND));

        // when
        ReservationException exception = assertThrows(ReservationException.class,
                () -> reviewService.write(Write.Request.builder().build(),
                        MemberBuilder.member()));

        // then
        assertEquals(RESERVATION_NOT_FOUND, exception.getErrorCode());
    }

    @Test
    @DisplayName("리뷰 작성 실패 - 승인된 예약이 아님")
    void write_not_confirm() {
        // given
        Member member = MemberBuilder.member();
        Store store = StoreBuilder.store();

        Reservation reservation = ReservationBuilder.reservation();
        reservation.addMemberAndStore(member, store);

        given(reservationService.getReservationFetchJoinOrThrow(any()))
                .willReturn(reservation);

        // when
        ReservationException exception = assertThrows(ReservationException.class,
                () -> reviewService.write(Write.Request.builder().build(),
                        MemberBuilder.member()));

        // then
        assertEquals(RESERVATION_NOT_CONFIRM, exception.getErrorCode());
    }

    @Test
    @DisplayName("리뷰 작성 실패 - 도착한 예약이 아님")
    void write_not_arrival() {
        // given
        Member member = MemberBuilder.member();
        Store store = StoreBuilder.store();

        Reservation reservation = ReservationBuilder.reservation();
        reservation.addMemberAndStore(member, store);
        reservation.updateReservationType(ReservationType.CONFIRM);

        given(reservationService.getReservationFetchJoinOrThrow(any()))
                .willReturn(reservation);

        // when
        ReservationException exception = assertThrows(ReservationException.class,
                () -> reviewService.write(Write.Request.builder().build(),
                        MemberBuilder.member()));

        // then
        assertEquals(RESERVATION_NOT_VISITED, exception.getErrorCode());
    }

    @Test
    @DisplayName("리뷰 작성 실패 - 예약한 회원이 아님")
    void write_owner_not_equal() {
        // given
        Member member = Member.builder()
                .email("test2@gmail.com")
                .build();
        Store store = StoreBuilder.store();

        Reservation reservation = ReservationBuilder.reservation();
        reservation.addMemberAndStore(member, store);
        reservation.updateReservationType(ReservationType.CONFIRM);
        reservation.updateArrival();

        given(reservationService.getReservationFetchJoinOrThrow(any()))
                .willReturn(reservation);

        // when
        ReservationException exception = assertThrows(ReservationException.class,
                () -> reviewService.write(Write.Request.builder().build(),
                        MemberBuilder.member()));

        // then
        assertEquals(RESERVATION_ACCESS_DENY, exception.getErrorCode());
    }

    @Test
    @DisplayName("리뷰 수정 성공")
    void update_success() {
        // given
        Member member = MemberBuilder.member();
        Store store = StoreBuilder.store();

        String contents = "맛있어요";
        Review review = Review.builder()
                .contents(contents)
                .score(SCORE)
                .build();
        review.addMemberAndStore(member, store);

        given(reviewRepository.findByIdFetchJoin(any()))
                .willReturn(Optional.of(review));

        // when
        ReviewDto reviewDto = reviewService.update(
                Update.Request.builder()
                        .contents(contents)
                        .score(SCORE)
                        .build());

        // then
        assertEquals(contents, reviewDto.getContents());
        assertEquals(SCORE, reviewDto.getScore());
    }

    @Test
    @DisplayName("리뷰 수정 실패 - 존재하지 않는 리뷰")
    void update_review_not_found() {
        // given
        given(reviewRepository.findByIdFetchJoin(any()))
                .willReturn(Optional.empty());

        // when
        ReviewException exception = assertThrows(ReviewException.class, () ->
                reviewService.update(Update.Request.builder().build()));

        // then
        assertEquals(REVIEW_NOT_FOUND, exception.getErrorCode());
    }

    @Test
    @DisplayName("리뷰 삭제 성공 - 동일한 유저")
    void delete_success() {
        // given
        Member member = MemberBuilder.member();
        Store store = StoreBuilder.store();

        Review review = ReviewBuilder.review();
        review.addMemberAndStore(member, store);

        given(reviewRepository.findByIdFetchJoin(any()))
                .willReturn(Optional.of(review));

        given(memberService.findByEmailOrThrow(any()))
                .willReturn(member);

        doNothing().when(reviewRepository).deleteByReviewId(any());

        // when
        Long reviewId = reviewService.delete(ID, member);

        // then
        assertEquals(ID, reviewId);
    }

    @Test
    @DisplayName("리뷰 삭제 성공 - 매니저 권한")
    void delete_success_manager() {
        // given
        String email = "manager@gamil.com";
        Member member = Member.builder()
                .email(email)
                .role(Role.MANAGER)
                .build();

        Store store = StoreBuilder.store();
        store.addMember(member);

        Review review = ReviewBuilder.review();
        review.addMemberAndStore(MemberBuilder.member(), store);

        given(reviewRepository.findByIdFetchJoin(any()))
                .willReturn(Optional.of(review));

        given(memberService.findByEmailOrThrow(any()))
                .willReturn(member);

        doNothing().when(reviewRepository).deleteByReviewId(any());

        // when
        Long reviewId = reviewService.delete(ID, member);

        // then
        assertEquals(ID, reviewId);
    }

    @Test
    @DisplayName("리뷰 삭제 실패 - 존재하지 않는 리뷰")
    void delete_review_not_found() {
        // given
        given(reviewRepository.findByIdFetchJoin(any()))
                .willReturn(Optional.empty());

        // when
        ReviewException exception = assertThrows(ReviewException.class, () ->
                reviewService.delete(ID, MemberBuilder.member()));

        // then
        assertEquals(REVIEW_NOT_FOUND, exception.getErrorCode());
    }

    @Test
    @DisplayName("리뷰 삭제 실패 - 해당 매장의 매니저가 아님")
    void delete_manager_not_match() {
        // given
        String email = "manager@gamil.com";
        Member manager = Member.builder()
                .email(email)
                .role(Role.MANAGER)
                .build();

        Member member = MemberBuilder.member();
        Store store = StoreBuilder.store();
        store.addMember(member);

        Review review = ReviewBuilder.review();
        review.addMemberAndStore(member, store);

        given(reviewRepository.findByIdFetchJoin(any()))
                .willReturn(Optional.of(review));

        given(memberService.findByEmailOrThrow(any()))
                .willReturn(manager);

        // when
        ReviewException exception = assertThrows(ReviewException.class, () ->
                reviewService.delete(ID, MemberBuilder.member()));

        // then
        assertEquals(REVIEW_ACCESS_DENY, exception.getErrorCode());
    }

    @Test
    @DisplayName("리뷰 삭제 실패 - 동일한 유저가 아님")
    void delete_user_not_match() {
        // given
        String email = "other@gamil.com";
        Member other = Member.builder()
                .email(email)
                .build();

        Member member = MemberBuilder.member();
        Store store = StoreBuilder.store();

        Review review = ReviewBuilder.review();
        review.addMemberAndStore(member, store);

        given(reviewRepository.findByIdFetchJoin(any()))
                .willReturn(Optional.of(review));

        given(memberService.findByEmailOrThrow(any()))
                .willReturn(other);

        // when
        ReviewException exception = assertThrows(ReviewException.class, () ->
                reviewService.delete(ID, MemberBuilder.member()));

        // then
        assertEquals(REVIEW_ACCESS_DENY, exception.getErrorCode());
    }
}