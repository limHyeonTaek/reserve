package com.zerobase.reservation.domain.common.builder.dto;

import com.zerobase.reservation.domain.common.builder.MemberBuilder;
import com.zerobase.reservation.domain.review.dto.model.ReviewDto;

import static com.zerobase.reservation.domain.common.constants.ReviewConstants.*;

public class ReviewDtoBuilder {
    public static ReviewDto reviewDto() {
        return ReviewDto.builder()
                .reviewId(ID)
                .contents(CONTENTS)
                .score(SCORE)
                .member(MemberBuilder.member())
                .build();
    }
}
