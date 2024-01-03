package com.zerobase.reservation.domain.member.exception;

import com.zerobase.reservation.general.exception.CustomException;
import com.zerobase.reservation.general.exception.ErrorCode;

/**
 회원 도메인에서 발생하는 예외를 담습니다.
 */
public class MemberException extends CustomException {
    public MemberException(ErrorCode errorCode) {
        super(errorCode);
    }

    public MemberException(ErrorCode errorCode, String msg) {
        super(errorCode, msg);
    }
}
