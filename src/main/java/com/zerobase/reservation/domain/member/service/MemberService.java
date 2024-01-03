package com.zerobase.reservation.domain.member.service;

import com.zerobase.reservation.domain.member.entity.Member;
import com.zerobase.reservation.domain.member.exception.MemberException;
import com.zerobase.reservation.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.zerobase.reservation.general.exception.ErrorCode.MEMBER_NOT_FOUND;

/**
 타 도메인의 서비스 객체에서 공통적으로 사용하기 위한 회원 서비스입니다.
 */
@RequiredArgsConstructor
@Service
public class MemberService {
    private final MemberRepository memberRepository;

    public Member findByMemberKeyOrThrow(String memberKey) {
        return memberRepository.findByMemberKey(memberKey)
                .orElseThrow(() -> new MemberException(MEMBER_NOT_FOUND));
    }

    public Member findByPhoneNumberOrThrow(String phoneNumber) {
        return memberRepository.findByPhoneNumber(phoneNumber)
                .orElseThrow(() -> new MemberException(MEMBER_NOT_FOUND));
    }

    public Member findByEmailOrThrow(String email) {
        return memberRepository.findByEmail(email)
                .orElseThrow(() -> new MemberException(MEMBER_NOT_FOUND));
    }
}
