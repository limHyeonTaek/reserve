package com.zerobase.reservation.general.exception;

public record ErrorResponse(
        ErrorCode errorCode,
        String message
) {

}
