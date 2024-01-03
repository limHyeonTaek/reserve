package com.zerobase.reservation.domain.review.service;

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
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.zerobase.reservation.general.exception.ErrorCode.*;

@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final ReservationService reservationService;
    private final MemberService memberService;

    /**
     리뷰 작성
     리뷰 작성을 요청한 예약에 대해 방문 완료한 예약인지 확인
     예약자와 리뷰 요청 자가 같은지 검증 후 리뷰를 작성
     */
    @Transactional
    public ReviewDto write(Write.Request request, UserDetails userDetails) {
        Reservation reservation = reservationService
                .getReservationFetchJoinOrThrow(request.getReservationKey());

        validateReservation(reservation, userDetails);

        return ReviewDto.fromEntity(
                reviewRepository.save(request.toEntity(reservation)));
    }

    private static void validateReservation(Reservation reservation,
                                            UserDetails userDetails) {
        if (reservation.getReservationType() != ReservationType.CONFIRM) {
            throw new ReservationException(RESERVATION_NOT_CONFIRM);
        }

        if (!reservation.isArrival()) {
            throw new ReservationException(RESERVATION_NOT_VISITED);
        }

        if (!reservation.getMember().getEmail().equals(userDetails.getUsername())) {
            throw new ReservationException(RESERVATION_ACCESS_DENY);
        }
    }

    /**
     리뷰 수정
     수정을 요청한 유저와 리뷰를 작성한 유저가 다를 경우 리뷰 수정에 실패
     회원을 조회하는 쿼리를 대신하여 인증된 사용자의 정보로 검증
     */
    @PostAuthorize("isAuthenticated() " +
            "and returnObject.member.email == principal.username")
    @Transactional
    public ReviewDto update(Update.Request request) {
        Review review = findByIdOrThrow(request.getReviewId());
        review.updateReview(request);
        return ReviewDto.fromEntity(review);
    }

    /**
     리뷰 삭제
     로그인한 사용자가 일반 사용자(USER)일 경우 리뷰 작성자와 같은지 확인
     로그인한 사용자가 매니저일 경우 리뷰가 등록된 매장의 점주와 같은지 확인
     */
    @Transactional
    public Long delete(Long reviewId, UserDetails userDetails) {
        Review review = findByIdOrThrow(reviewId);

        validateMember(userDetails, review);

        reviewRepository.deleteByReviewId(reviewId);
        return reviewId;
    }

    private void validateMember(UserDetails userDetails, Review review) {
        Member principal =
                memberService.findByEmailOrThrow(userDetails.getUsername());

        if (principal.getRole() == Role.MANAGER) {
            if (!review.getStore().getMember().getEmail()
                    .equals(principal.getEmail())) {
                throw new ReviewException(REVIEW_ACCESS_DENY);
            }
        } else {
            if (!review.getMember().getEmail().equals(principal.getEmail())) {
                throw new ReviewException(REVIEW_ACCESS_DENY);
            }
        }
    }

    private Review findByIdOrThrow(Long reviewId) {
        return reviewRepository.findByIdFetchJoin(reviewId)
                .orElseThrow(() -> new ReviewException(REVIEW_NOT_FOUND));
    }
}
