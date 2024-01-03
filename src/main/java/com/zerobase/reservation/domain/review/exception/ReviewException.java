package com.zerobase.reservation.domain.review.exception;

import com.zerobase.reservation.general.exception.CustomException;
import com.zerobase.reservation.general.exception.ErrorCode;

/**
 리뷰 도메인에서 발생하는 예외를 담습니다.
 */
public class ReviewException extends CustomException {
    public ReviewException(ErrorCode errorCode) {
        super(errorCode);
    }
}
