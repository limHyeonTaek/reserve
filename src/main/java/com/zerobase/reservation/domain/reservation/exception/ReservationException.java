package com.zerobase.reservation.domain.reservation.exception;

import com.zerobase.reservation.general.exception.CustomException;
import com.zerobase.reservation.general.exception.ErrorCode;

/**
 예약 도메인에서 발생하는 예외를 담습니다.
 */
public class ReservationException extends CustomException {
    public ReservationException(ErrorCode errorCode) {
        super(errorCode);
    }

    public ReservationException(ErrorCode errorCode, String msg) {
        super(errorCode, msg);
    }
}
