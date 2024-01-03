package com.zerobase.reservation.domain.review.dto;

import com.zerobase.reservation.domain.reservation.entity.Reservation;
import com.zerobase.reservation.domain.review.dto.model.ReviewDto;
import com.zerobase.reservation.domain.review.entity.Review;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class Write {
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Request {
        @NotBlank
        private String reservationKey;

        @NotBlank
        private String contents;

        @NotNull
        private Double score;

        public Review toEntity(Reservation reservation) {
            Review review = Review.builder()
                    .contents(contents)
                    .score(score)
                    .build();

            review.addMemberAndStore(reservation.getMember(),
                    reservation.getStore());
            return review;
        }
    }

    public record Response(Long reviewId) {
        public static Response from(ReviewDto reviewDto) {
            return new Response(reviewDto.getReviewId());
        }
    }
}
