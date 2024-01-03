package com.zerobase.reservation.domain.common.builder;

import com.zerobase.reservation.domain.review.entity.Review;

import static com.zerobase.reservation.domain.common.constants.ReviewConstants.CONTENTS;
import static com.zerobase.reservation.domain.common.constants.ReviewConstants.SCORE;

public class ReviewBuilder {
    public static Review review() {
        return Review.builder()
                .contents(CONTENTS)
                .score(SCORE)
                .build();
    }
}
