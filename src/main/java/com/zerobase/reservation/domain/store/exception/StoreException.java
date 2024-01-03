package com.zerobase.reservation.domain.store.exception;

import com.zerobase.reservation.general.exception.CustomException;
import com.zerobase.reservation.general.exception.ErrorCode;

/**
 매장 도메인에서 발생하는 예외를 담습니다.
 */
public class StoreException extends CustomException {
    public StoreException(ErrorCode errorCode) {
        super(errorCode);
    }
}
