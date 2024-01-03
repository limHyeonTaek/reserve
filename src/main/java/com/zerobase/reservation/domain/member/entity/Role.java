package com.zerobase.reservation.domain.member.entity;

import com.zerobase.reservation.domain.member.exception.MemberException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

import static com.zerobase.reservation.general.exception.ErrorCode.INVALID_REQUEST;

@Getter
@RequiredArgsConstructor
public enum Role {
    USER("ROLE_USER"),
    MANAGER("ROLE_MANAGER");

    private final String key;

    public static Role fromKey(String key) {
        return Arrays.stream(values())
                .filter(o -> o.getKey().equals(key))
                .findFirst()
                .orElseThrow(() -> new MemberException(
                        INVALID_REQUEST, "잘못된 권한 타입입니다."));
    }
}
