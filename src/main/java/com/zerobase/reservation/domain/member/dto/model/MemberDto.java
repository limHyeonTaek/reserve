package com.zerobase.reservation.domain.member.dto.model;

import com.zerobase.reservation.domain.member.entity.Member;
import com.zerobase.reservation.domain.member.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberDto {
    private String memberKey;
    private String name;
    private String email;
    private Role role;

    public static MemberDto fromEntity(Member member) {
        return MemberDto.builder()
                .memberKey(member.getMemberKey())
                .name(member.getName())
                .email(member.getEmail())
                .role(member.getRole())
                .build();
    }
}
