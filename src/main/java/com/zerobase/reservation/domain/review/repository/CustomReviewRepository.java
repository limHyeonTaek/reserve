package com.zerobase.reservation.domain.review.repository;

import com.zerobase.reservation.domain.review.entity.Review;

import java.util.Optional;

public interface CustomReviewRepository {
    Optional<Review> findByIdFetchJoin(Long id);

    /**
     리뷰 id로 리뷰를 삭제하는 쿼리
     */
    void deleteByReviewId(Long id);
}
