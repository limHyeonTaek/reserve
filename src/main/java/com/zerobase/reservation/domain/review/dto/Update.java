package com.zerobase.reservation.domain.review.dto;

import com.zerobase.reservation.domain.review.dto.model.ReviewDto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class Update {
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Request {
        @NotNull
        private Long reviewId;

        @NotBlank
        private String contents;

        @NotNull
        private Double score;
    }

    public record Response(Long reviewId) {
        public static Response from(ReviewDto reviewDto) {
            return new Response(reviewDto.getReviewId());
        }
    }
}
