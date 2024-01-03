package com.zerobase.reservation.domain.review.dto;

public record DeleteResponse(Long reviewId) {
    public static DeleteResponse from(Long reviewId) {
        return new DeleteResponse(reviewId);
    }
}
