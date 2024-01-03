package com.zerobase.reservation.domain.common.builder.dto;

import com.zerobase.reservation.domain.member.dto.model.MemberDto;
import com.zerobase.reservation.domain.member.entity.Role;

import static com.zerobase.reservation.domain.common.constants.MemberConstants.EMAIL;
import static com.zerobase.reservation.domain.common.constants.MemberConstants.MEMBER_NAME;

public class MemberDtoBuilder {
    public static MemberDto memberDto() {
        return MemberDto.builder()
                .name(MEMBER_NAME)
                .email(EMAIL)
                .role(Role.USER)
                .build();
    }
}
