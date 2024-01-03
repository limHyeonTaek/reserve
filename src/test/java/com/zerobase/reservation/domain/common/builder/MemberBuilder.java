package com.zerobase.reservation.domain.common.builder;

import com.zerobase.reservation.domain.member.entity.Member;
import com.zerobase.reservation.domain.member.entity.Role;

import static com.zerobase.reservation.domain.common.constants.MemberConstants.*;
import static com.zerobase.reservation.domain.common.constants.MemberConstants.PHONE_NUMBER;

public class MemberBuilder {
    public static Member member() {
        return Member.builder()
                .memberKey(MEMBER_KEY)
                .name(MEMBER_NAME)
                .email(EMAIL)
                .password(PASSWORD)
                .phoneNumber(PHONE_NUMBER)
                .role(Role.USER)
                .build();
    }
}
