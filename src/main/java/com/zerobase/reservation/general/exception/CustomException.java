package com.zerobase.reservation.general.exception;

import lombok.Getter;

/**
 커스텀 예외를 처리하기 위한 추상 클래스
 */
@Getter
public abstract class CustomException extends RuntimeException {
    private final ErrorCode errorCode;
    private final String message;

    public CustomException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
        this.message = errorCode.getMessage();
    }

    public CustomException(ErrorCode errorCode, String msg) {
        super(msg);
        this.errorCode = errorCode;
        this.message = msg;
    }
}
