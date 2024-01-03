package com.zerobase.reservation.domain.review.repository;

import com.zerobase.reservation.domain.review.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long>, CustomReviewRepository {
}
